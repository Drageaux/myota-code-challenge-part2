###README

- Navigate into the src folder of this project via the command line
- Run "javac Main.java" to compile the program
- Run "java Main" to use to program
- Follow the menu and enter inputs as prompted


  1. (when file not opened) Open a file to start program
    - enter file name
      - choose either (1) read mode or (2) write mode
  2. (write-mode only) Enable writing to file (can create new file)
    - if write successful, flush(), a.k.a store chunks and generate metadata of new version
  3. (either mode) Read all file data with offset of 0 to demonstrate sanity
    - prints both in hex and ASCII
  4. (either mode) Restore up to 2 versions prior to the current one
    - also append version number to file
  5. Close file and renable option 1
  0. Exit the program