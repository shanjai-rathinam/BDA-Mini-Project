# Global Weather Analysis using Hadoop MapReduce

[![Built with Maven](https://img.shields.io/badge/Build-Maven-brightgreen.svg)](https://maven.apache.org/)
[![Language-Java](https://img.shields.io/badge/Language-Java-orange.svg)]()
[![Environment-Codespaces](https://img.shields.io/badge/Developed%20on-Codespaces-blue.svg)](https://github.com/features/codespaces)

## Project Overview

This project is a high-performance Big Data analysis application built on the **Hadoop MapReduce** framework. Its purpose is to process massive volumes of historical weather data, stored in the Hadoop Distributed File System (HDFS), to extract meaningful meteorological insights through parallel data aggregation.

This entire application was developed, configured, and successfully built in a cloud-based **GitHub Codespace**, demonstrating proficiency with modern distributed systems tooling.

## Core Objective

The primary goal of the MapReduce job is to **identify the maximum recorded temperature for each year** present in the historical weather dataset.

## Technologies and Tools

| Tool | Purpose |
| :--- | :--- |
| **Hadoop MapReduce** | The core programming model for parallel and distributed data processing. |
| **Java 8+** | The programming language used for the Mapper, Reducer, and Driver logic. |
| **Apache Maven** | Used for dependency management (Hadoop libraries) and packaging the executable JAR. |
| **GitHub Codespaces**| The cloud-based development environment used to write and build the entire project. |

## Data Format

The application is designed to process data from the **NOAA Global Historical Climatology Network Daily (GHCN-D)** dataset, which uses a fixed-width format. The `Mapper` specifically parses the record to extract the following fields for processing:

| Field | Position (0-Indexed) | Value |
| :--- | :--- | :--- |
| **Year** | `15` to `19` | Used as the Key for aggregation. |
| **Temperature** | `87` to `92` | Used as the Value for aggregation (in tenths of a degree Celsius). |
| **Quality Flag** | `92` | Used to filter out poor quality or missing data. |

---

## Technical Implementation

The project consists of three main Java classes packaged under the `com.shanjai-rathinam.weather` package:

1.  **`MaxTemperatureMapper.java`**:
    *   **Input:** Line-by-line weather records.
    *   **Logic:** Parses the record, extracts the year and temperature, filters out missing values (`9999`) and poor-quality readings, and emits the intermediate pair: **`(Text Year, IntWritable Temperature)`**.

2.  **`MaxTemperatureReducer.java`**:
    *   **Input:** A year and a list of all temperature readings for that year.
    *   **Logic:** Iterates through the list of temperatures and uses `Math.max()` to find the highest value.
    *   **Output:** The final aggregated pair: **`(Text Year, IntWritable MaxTemperature)`**.
    *   **Optimization:** This class is also used as the **Combiner** to perform local aggregation on the map nodes, significantly reducing network I/O.

3.  **`MaxTemperatureDriver.java`**:
    *   **Logic:** Configures the MapReduce job, sets the input/output paths, and assigns the Mapper, Reducer, and Combiner classes.

---

## Building and Deployment

### 1. Build Instructions (Completed)

To build the executable JAR file (`.jar-with-dependencies`) from the source code, run the following Maven command from the project root:

```bash
# This command compiles the code and packages it with all necessary Hadoop dependencies.
mvn clean package
