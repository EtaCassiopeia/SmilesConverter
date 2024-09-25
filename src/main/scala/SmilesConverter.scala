import zio._
import zio.Console
import org.openscience.cdk.DefaultChemObjectBuilder
import org.openscience.cdk.interfaces._
import org.openscience.cdk.smiles._
import org.openscience.cdk.renderer._
import org.openscience.cdk.renderer.generators._
import org.openscience.cdk.renderer.font.AWTFontManager
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor
import org.openscience.cdk.layout.StructureDiagramGenerator
import org.openscience.cdk.smiles.{SmiFlavor, SmilesGenerator}
import uk.ac.cam.ch.wwmm.opsin.NameToStructure
import uk.ac.cam.ch.wwmm.opsin.OpsinResult

import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.Rectangle
import javax.imageio.ImageIO
import java.io.File

object SmilesConverter extends ZIOAppDefault {

  def run = {
    val program = for {
      _     <- Console.printLine("Enter molecule name or SMILES notation:")
      input <- Console.readLine
      _     <- processInput(input)
    } yield ()

    program
  }

  def processInput(input: String): ZIO[Any, Throwable, Unit] = {
    if (input.matches(".*[a-zA-Z].*")) {
      // Assume it's a molecule name
      for {
        smiles         <- convertNameToSmiles(input)
        _              <- Console.printLine(s"SMILES notation: $smiles")
        mol            <- parseSmiles(smiles)
        canonicalSmiles <- generateSmiles(mol, isomeric = false)
        isomericSmiles <- generateSmiles(mol, isomeric = true)
        _              <- Console.printLine(s"Canonical SMILES: $canonicalSmiles")
        _              <- Console.printLine(s"Isomeric SMILES: $isomericSmiles")
        _              <- drawMolecule(mol, "output.png")
        _              <- Console.printLine("Molecule image saved as output.png")
      } yield ()
    } else {
      // Assume it's SMILES notation
      for {
        mol            <- parseSmiles(input)
        canonicalSmiles <- generateSmiles(mol, isomeric = false)
        isomericSmiles <- generateSmiles(mol, isomeric = true)
        _              <- Console.printLine(s"Canonical SMILES: $canonicalSmiles")
        _              <- Console.printLine(s"Isomeric SMILES: $isomericSmiles")
        name           <- convertSmilesToName(mol)
        _              <- Console.printLine(s"Molecule name: $name")
        _              <- drawMolecule(mol, "output.png")
        _              <- Console.printLine("Molecule image saved as output.png")
      } yield ()
    }
  }

  def convertNameToSmiles(name: String): Task[String] = ZIO.attempt {
    val n2s = NameToStructure.getInstance()
    val result: OpsinResult = n2s.parseChemicalName(name)
    if (result.getStatus == OpsinResult.OPSIN_RESULT_STATUS.SUCCESS) {
      result.getSmiles
    } else {
      throw new Exception("Unable to parse molecule name.")
    }
  }

  def parseSmiles(smiles: String): Task[IAtomContainer] = ZIO.attempt {
    val builder = DefaultChemObjectBuilder.getInstance()
    val smilesParser = new SmilesParser(builder)
    smilesParser.parseSmiles(smiles)
  }

  def generateSmiles(mol: IAtomContainer, isomeric: Boolean): Task[String] = ZIO.attempt {
    val flavor = if (isomeric) {
      SmiFlavor.Unique | SmiFlavor.Absolute
    } else {
      SmiFlavor.Unique
    }
    val generator = new SmilesGenerator(flavor)
    generator.create(mol)
  }

  def convertSmilesToName(mol: IAtomContainer): Task[String] = ZIO.attempt {
    // CDK does not support name generation directly.
    // This is a placeholder; generating IUPAC names from structures requires additional tools.
    "Name generation not implemented."
  }

  def drawMolecule(mol: IAtomContainer, filename: String): Task[Unit] = ZIO.attempt {
    val sdg = new StructureDiagramGenerator()
    sdg.setMolecule(mol)
    sdg.generateCoordinates()
    val moleculeWithCoords = sdg.getMolecule()

    val imageWidth = 400
    val imageHeight = 400

    val image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)
    val g2 = image.createGraphics()
    g2.setColor(Color.WHITE)
    g2.fillRect(0, 0, imageWidth, imageHeight)

    val generators = java.util.Arrays.asList(
      new BasicSceneGenerator(),
      new BasicBondGenerator(),
      new BasicAtomGenerator()
    )
    val renderer = new AtomContainerRenderer(generators, new AWTFontManager())
    renderer.setup(moleculeWithCoords, new Rectangle(0, 0, imageWidth, imageHeight))

    renderer.paint(moleculeWithCoords, new AWTDrawVisitor(g2),
      new Rectangle2D.Double(0, 0, imageWidth, imageHeight), true)

    g2.dispose()
    ImageIO.write(image, "png", new File(filename))
  }
}
