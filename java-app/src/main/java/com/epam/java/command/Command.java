
package com.epam.java.command;

public interface Command {

    void start(String[] args) throws SpecificExceptionForStart;

    void stop() throws SpecificExceptionForStop;
}

class SpecificExceptionForStart extends Exception {
   // define this exception with appropriate message or behavior
}

class SpecificExceptionForStop extends Exception {
   // define this exception with appropriate message or behavior
}
