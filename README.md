# Java Socket Backend for Multi-language Code Processing

## 🎯 Objective
Develop a socket-based backend in Java to:
- Accept source code from frontend via TCP.
- Identify language and action (Format / Run).
- Process Java, Python, and JavaScript code.
- Return output or formatted code to the client.

## 🧰 Technologies Used
- Java Sockets (Server)
- Runtime execution (`javac`, `java`, `python3`, `node`)
- External formatters: `black` (Python), `prettier` (JavaScript), `google-java-format` (Java)

## 🧱 System Flow

```plaintext
Frontend GUI <--Socket--> Java Backend Server
                                 ├── Parse command
                                 ├── Save temp file
                                 ├── Format or run code
                                 └── Send response back
