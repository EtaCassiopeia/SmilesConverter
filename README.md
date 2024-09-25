# SmilesConverter

**SmilesConverter** is a Scala application built using **ZIO** and the **Chemistry Development Kit (CDK)**. It enables users to:

- Convert chemical names to SMILES notation.
- Convert SMILES notation to chemical names (placeholder functionality).
- Generate canonical and isomeric SMILES representations.
- Visualize molecular structures by drawing them as images.

![SmilesConverter](images/output.png)

---

## What is SMILES?

**SMILES (Simplified Molecular Input Line Entry System)** is a specification for describing the structure of chemical molecules using short ASCII strings. It encodes molecular structures in a way that is both human-readable and machine-processable.

### Building Blocks of SMILES

SMILES notation is composed of several fundamental elements:

- **Atoms**:
    - Represented by their atomic symbols (e.g., `C` for carbon, `O` for oxygen).
    - Hydrogen atoms attached to other atoms are usually implicit.
    - Special atoms or those with explicit hydrogens, charges, or isotopes are enclosed in square brackets, e.g., `[Na+]`, `[13C]`.

- **Bonds**:
    - **Single bond**: `-` or implied if omitted.
    - **Double bond**: `=`
    - **Triple bond**: `#`
    - **Aromatic bond**: Represented by lowercase letters for aromatic atoms (e.g., `c`, `n`).

- **Branches**:
    - Parentheses `()` denote branches off the main chain.
    - Example: `CC(O)C` represents a molecule where an OH group branches off the second carbon atom.

- **Ring Closures**:
    - Numbers indicate ring connections.
    - Example: `C1CCCCC1` represents cyclohexane, a six-membered ring.

- **Stereochemistry**:
    - Chiral centers are indicated using `@` symbols.
    - Double bond geometries use `/` and `\` to represent cis/trans isomerism.

- **Examples**:
    - **Ethanol**: `CCO`
    - **Benzene**: `c1ccccc1`
    - **Glucose**: `OC[C@H](O)[C@@H](O)[C@@H](O)[C@H](O)C=O`

For more detailed information, refer to the [Daylight Theory Manual on SMILES](https://www.daylight.com/dayhtml/doc/theory/theory.smiles.html).

---

## Features

- **Chemical Name to SMILES Conversion**: Converts IUPAC names to SMILES using OPSIN.
- **SMILES to Molecule Parsing**: Parses SMILES strings into molecular structures with CDK.
- **Canonical and Isomeric SMILES Generation**: Produces both standard and stereochemically detailed SMILES.
- **Molecule Visualization**: Draws 2D images of molecules and saves them as PNG files.
- **Console Interface**: Interacts with users via a command-line interface.

---

## Running the Application

**Compile and Run**:

   Open a terminal in the project directory and execute:

   ```bash
   sbt run
   ```

---

## Usage Examples

### Example: Chemical Name to SMILES Conversion

**Input**:

```
Enter molecule name or SMILES notation:
glucose
```

**Output**:

```
SMILES notation: O=C[C@H](O)[C@@H](O)[C@H](O)[C@H](O)CO
Canonical SMILES: O=CC(O)C(O)C(O)C(O)CO
Isomeric SMILES: C(=O)[C@@H]([C@H]([C@@H]([C@@H](CO)O)O)O)O
Molecule image saved as output.png
```

- **Description**:

    - The application converts "glucose" into its SMILES notation.
    - Generates canonical and isomeric SMILES (identical in this case due to lack of stereochemistry).
    - Saves the molecular structure as `output.png`.

---

## Limitations

- The application cannot generate chemical names from SMILES.
- Limited error handling for invalid inputs.
- Only 2D molecular structures are generated.
---

## References

- **SMILES Theory**: [Daylight Theory Manual](https://www.daylight.com/dayhtml/doc/theory/theory.smiles.html)
- **Chemistry Development Kit (CDK)**: [CDK GitHub](https://github.com/cdk)
- **OPSIN**: [OPSIN GitHub](https://github.com/dan2097/opsin)
