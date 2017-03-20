package espresso.boolFunction;

import espresso.boolFunction.cube.Cube;
import org.junit.Test;

import static espresso.boolFunction.InputState.*;
import static espresso.boolFunction.OutputState.*;
import static org.junit.Assert.*;

public class CubeTest {

  @Test
  public void complement() throws Exception {
    Cube cube = new Cube(new InputState[]{ONE, ZERO, DONTCARE}, new OutputState[]{OUTPUT, NOT_OUTPUT});
    Cover actualComplement = cube.complement();
    Cover expectedComplement = new Cover(
        new Cube(new InputState[]{ZERO, DONTCARE, DONTCARE}, new OutputState[]{OUTPUT, NOT_OUTPUT}),
        new Cube(new InputState[]{DONTCARE, ONE, DONTCARE}, new OutputState[]{OUTPUT, NOT_OUTPUT})
    );

    assertTrue(
        "Size of the complement is wrong.",
        actualComplement.size() == expectedComplement.size()
    );
    assertTrue(
        "Complementing a cube is apparently not working.",
        actualComplement.get(0).equals(expectedComplement.get(0))
    );
    assertTrue(
        "Complementing a cube is apparently not working.",
        actualComplement.get(1).equals(expectedComplement.get(1))
    );
  }
}