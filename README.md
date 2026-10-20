# Digital Signal Generator & Data Communication Analyzer

A Java-based Data Communication engineering toolkit designed for digital transmission analysis, analog-to-digital signal conversion, line coding, scrambling, and waveform plotting.

---

## 📌 Project Overview

In digital data communication, binary data must be transformed into physical signals capable of traveling through transmission media. This project implements and visualizes key physical and data link layer concepts:

1. **Digital Data Generation**: Random bit generation, sequences with designated zero runs, or custom user-defined bitstreams.
2. **Algorithmic Data Analysis**: Longest palindromic substring discovery in the bitstream using **Manacher's Algorithm** ($O(n)$ time complexity).
3. **Analog-to-Digital Modulation**:
   - **PCM (Pulse Code Modulation)**: Uniform midpoint quantization with configurable bit resolution ($1$ to $16$ bits/sample) and binary encoding.
   - **Delta Modulation (DM)**: 1-bit differential staircase modulation with custom step size ($\Delta$).
4. **Line Coding Schemes**:
   - **NRZ-L** (Non-Return to Zero Level)
   - **NRZ-I** (Non-Return to Zero Invert on 1)
   - **Manchester** (Biphase, IEEE 802.3 convention)
   - **Differential Manchester** (Token Ring IEEE 802.5 convention)
   - **AMI** (Alternate Mark Inversion)
5. **Scrambling & Descrambling**:
   - **B8ZS** (Bipolar with 8-Zero Substitution, North American standard)
   - **HDB3** (High-Density Bipolar 3-Zero, European standard)
6. **Transmission Metrics**:
   - Signal rate (Baud Rate $S = N / r$)
   - DC Component calculation (average voltage)
   - Transition count and transition density (clock recovery metric)
   - Maximum consecutive zero run-length comparison
7. **Signal Visualization**:
   - Java Swing GUI plotting digital waveforms, PCM staircases, and DM reconstructions with zero-reference baseline and sample index ticks.

---

## 🗂 Architecture & Directory Structure

```text
Digital_Signal_Generator/
├── src/
│   ├── Main.java                          # Interactive CLI application & analysis
│   ├── ProjectTest.java                   # Unified verification test suite (20 tests)
│   ├── EncodingDecoderTest.java           # Line encoding/decoding verification
│   ├── B8ZSKnownCasesTest.java            # B8ZS substitution verification
│   ├── HDB3KnownCasesTest.java            # HDB3 substitution verification
│   ├── ScramblingTest.java                # Long-run zero comparison test
│   ├── DeltaModulationTest.java           # DM staircase & MSE analysis
│   ├── SignalPlotterTest.java             # Swing plotter test runner
│   │
│   ├── data/                              # Data generation & algorithms
│   │   ├── DataType.java                  # Pattern enum (RANDOM, FOUR_ZEROS, EIGHT_ZEROS)
│   │   ├── DigitalDataGenerator.java      # Bitstream generator
│   │   └── PalindromeFinder.java          # Manacher's algorithm implementation
│   │
│   ├── encoding/                          # Line encoder implementations
│   │   ├── LineEncoder.java               # Encoder interface
│   │   ├── NRZL.java                      # NRZ-L encoder
│   │   ├── NRZI.java                      # NRZ-I encoder
│   │   ├── Manchester.java                # Manchester encoder
│   │   ├── DifferentialManchester.java    # Differential Manchester encoder
│   │   └── AMI.java                       # Alternate Mark Inversion encoder
│   │
│   ├── decoding/                          # Line decoder implementations
│   │   ├── LineDecoder.java               # Decoder interface
│   │   ├── NRZLDecoder.java               # NRZ-L decoder
│   │   ├── NRZIDecoder.java               # NRZ-I decoder
│   │   ├── ManchesterDecoder.java         # Manchester decoder
│   │   ├── DifferentialManchesterDecoder.java # Differential Manchester decoder
│   │   └── AMIDecoder.java                # AMI decoder with violation detection
│   │
│   ├── scrambling/                        # Scrambling and descrambling
│   │   ├── Scrambler.java                 # Scrambler interface
│   │   ├── B8ZS.java                      # B8ZS scrambler (000VB0VB)
│   │   ├── B8ZSDescrambler.java           # B8ZS descrambler
│   │   ├── HDB3.java                      # HDB3 scrambler (000V / B00V)
│   │   └── HDB3Descrambler.java           # HDB3 descrambler
│   │
│   ├── modulation/                        # Analog-to-digital modulation
│   │   ├── PCMEncoder.java                # PCM quantizer & encoder
│   │   ├── PCMDecoder.java                # PCM midpoint reconstruction
│   │   ├── DeltaModulator.java            # DM 1-bit staircase modulator
│   │   └── DeltaDemodulator.java          # DM staircase reconstructor
│   │
│   └── visualization/                     # Swing GUI plotting
│       └── SignalPlotter.java             # Waveform plotter with grid & axes
│
└── README.md
```

---

## ⚡ Scrambling Rules Summary

### B8ZS (Bipolar with 8-Zero Substitution)
Used in T1 carrier systems to replace sequences of 8 consecutive zeros without disrupting DC balance:
- If previous pulse is **positive (+)**: substitutes with `0 0 0 + - 0 - +` ($000VB0VB$)
- If previous pulse is **negative (-)**: substitutes with `0 0 0 - + 0 + -` ($000VB0VB$)

### HDB3 (High-Density Bipolar 3-Zero)
Used in E1 carrier systems to replace sequences of 4 consecutive zeros:
- **Odd pulses** since last substitution: substitutes with `0 0 0 V` (V has same polarity as previous pulse)
- **Even pulses** since last substitution: substitutes with `B 0 0 V` (B alternates; V violates)

---

## 🚀 How to Compile & Run

### Prerequisites
- Java Development Kit (JDK 17 or higher, tested on JDK 23)

### 1. Compile
Compile all source files into the `bin/` directory:
```bash
# Windows PowerShell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

### 2. Run the Main Application
```bash
java -cp bin Main
```

### 3. Run the Unified Test Suite
```bash
java -cp bin ProjectTest
```

### 4. Run Dedicated Unit Tests
```bash
java -cp bin EncodingDecoderTest
java -cp bin B8ZSKnownCasesTest
java -cp bin HDB3KnownCasesTest
java -cp bin DeltaModulationTest
java -cp bin SignalPlotterTest
```

---

## 🧪 Sample Main Execution

```text
==============================================
   DIGITAL SIGNAL GENERATOR & ANALYZER
==============================================

Select input type:
1. Digital bitstream
2. Analog samples (PCM / Delta Modulation)
Choice (1-2): 1

Select digital data source:
1. Completely random
2. Random with four consecutive zeros
3. Random with eight consecutive zeros
4. Enter custom binary bitstream
Choice (1-4): 4
Enter binary bitstream (e.g. 1011000000001): 11000000001

==============================================
   DIGITAL BITSTREAM & PALINDROME ANALYSIS
==============================================
Bitstream:          11000000001
Bit count (N):      11
Longest palindrome: 1000000001
Palindrome length:  10

Select line-coding scheme:
1. NRZ-L (Non-Return to Zero Level)
2. NRZ-I (Non-Return to Zero Invert on 1)
3. Manchester (Biphase)
4. Differential Manchester
5. AMI (Alternate Mark Inversion)
Choice (1-5): 5

Apply scrambling (to eliminate long zero runs)?
1. No scrambling
2. B8ZS (Bipolar with 8-Zero Substitution)
3. HDB3 (High-Density Bipolar 3-Zero)
Choice (1-3): 2

==============================================
   TRANSMISSION & SIGNAL METRICS
==============================================
Scheme:              AMI + B8ZS
Transmitted signal:  [1, -1, 0, 0, 0, -1, 1, 0, 1, -1, 1]
Signal element count:11
Signal elements per bit: 1.00 (r = 1.00)
Baud Rate:               1.00 * BitRate (N)
DC Component (avg level): 0.0909 V (Non-zero DC)
Signal Transitions:      7 / 11 intervals (density: 0.64)
Max zero run in data:    8 zeros

==============================================
   RECEIVER DECODING & VERIFICATION
==============================================
Decoded bitstream:   11000000001
Round-trip status:   PASS (Exact match)
```
