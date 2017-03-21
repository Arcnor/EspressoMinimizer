package espresso.boolFunction;

import espresso.boolFunction.cube.Cube;
import org.junit.Test;

import java.util.Iterator;

import static espresso.boolFunction.InputState.*;
import static espresso.boolFunction.OutputState.*;
import static org.junit.Assert.*;


public class CoverTest {
  @Test
  public void addMethodShouldIncreaseBitColumnCount() {
    Cube cube1 = new Cube(new InputState[]{ZERO, DONTCARE, ONE}, new OutputState[]{OUTPUT});
    Cover cubes = new Cover(3, 1);
    cubes.add(cube1);

    assertTrue(
        "Number of ZERO bits in first column should be 1.",
        cubes.getZeroColumnCount(0) == 1
    );
    assertTrue(
        "Number of ONE bits in first column should be 0.",
        cubes.getOneColumnCount(0) == 0
    );
    assertTrue(
        "Number of both ZERO and ONE bits in the second column should be 0.",
        cubes.getOneColumnCount(1) == 0 && cubes.getZeroColumnCount(1) == 0
    );
  }

  @Test
  public void removeMethodShouldLowerBitColumnCount() {
    Cube cube1 = new Cube(new InputState[]{ZERO, DONTCARE, ONE}, new OutputState[]{OUTPUT});
    Cover cubes = new Cover(3, 1);
    cubes.add(cube1);
    cubes.remove(cube1);

    for (int i = 0; i < cubes.size(); ++i) {
      assertTrue(
          "Number of both ZERO and ONE bits anywhere should be 0.",
          cubes.getOneColumnCount(1) == 0 && cubes.getZeroColumnCount(1) == 0
      );
    }
  }

  @Test
  public void addAllShouldIncreaseBitColumnCount() {
    Cube cube1 = new Cube(new InputState[]{ZERO, DONTCARE, DONTCARE}, new OutputState[]{OUTPUT});
    Cube cube2 = new Cube(new InputState[]{ONE, ONE, DONTCARE}, new OutputState[]{OUTPUT});
    Cube cube3 = new Cube(new InputState[]{DONTCARE, ONE, ONE}, new OutputState[]{OUTPUT});

    Cover cubes = new Cover(cube1, cube2, cube3);

    int[] oneColumnCount = new int[cubes.inputCount()];
    int[] zeroColumnCount = new int[cubes.inputCount()];

    for (int i = 0; i < cubes.inputCount(); i++) {
      oneColumnCount[i] = cubes.getOneColumnCount(i);
      zeroColumnCount[i] = cubes.getZeroColumnCount(i);
    }

    assertArrayEquals(
        "The amount of ZERO bits in each column is not correct.",
        new int[]{1, 0, 0},
        zeroColumnCount
    );

    assertArrayEquals(
        "The amount of ONE bits in each column is not correct.",
        new int[]{1, 2, 1},
        oneColumnCount
    );
  }

  @Test
  public void iteratorRemoveShouldLowerBitColumnCount() {
    Cube cube1 = new Cube(new InputState[]{ZERO, DONTCARE, DONTCARE}, new OutputState[]{OUTPUT});
    Cube cube2 = new Cube(new InputState[]{ONE, ONE, DONTCARE}, new OutputState[]{OUTPUT});
    Cube cube3 = new Cube(new InputState[]{DONTCARE, ONE, ONE}, new OutputState[]{OUTPUT});

    Cover cubes = new Cover(cube1, cube2, cube3);

    for (Iterator<Cube> iterator = cubes.iterator(); iterator.hasNext(); ) {
      iterator.next();
      iterator.remove();
    }

    for (int i = 0; i < cubes.inputCount(); ++i) {
      assertEquals(
          "Number of ZERO bits should be 0 anywhere.",
          0,
          cubes.getZeroColumnCount(i)
      );
      assertEquals(
          "Number of ONE bits should be 0 anywhere.",
          0,
          cubes.getOneColumnCount(i)
      );
    }
  }

  @Test
  public void shannonCofactors() throws Exception {
    Cover cover = new Cover(
        new Cube(new InputState[]{ONE, ONE, ZERO, DONTCARE}, new OutputState[]{OUTPUT, OUTPUT}),
        new Cube(new InputState[]{ZERO, ONE, DONTCARE, ZERO}, new OutputState[]{OUTPUT, OUTPUT}),
        new Cube(new InputState[]{ONE, ONE, ONE, ONE}, new OutputState[]{OUTPUT, NOT_OUTPUT})
    );

    Cover[] cofactors = cover.shannonCofactors(3);

    assertTrue(
        "Negative cofactor size is not right.",
        cofactors[0].size() == 2
    );
    assertTrue(
        "Positive cofactor size is not right.",
        cofactors[1].size() == 2
    );

    Cover actualPositiveCofactor = cofactors[1];
    Cover actualNegativeCofactor = cofactors[0];

    Cover expectedPositiveCofactor = new Cover(
        new Cube(new InputState[]{ONE, ONE, ZERO, DONTCARE}, new OutputState[]{OUTPUT, OUTPUT}),
        new Cube(new InputState[]{ONE, ONE, ONE, DONTCARE}, new OutputState[]{OUTPUT, NOT_OUTPUT})
    );
    Cover expectedNegativeCofactor = new Cover(
        new Cube(new InputState[]{ONE, ONE, ZERO, DONTCARE}, new OutputState[]{OUTPUT, OUTPUT}),
        new Cube(new InputState[]{ZERO, ONE, DONTCARE, DONTCARE}, new OutputState[]{OUTPUT, OUTPUT})
    );

    assertEquals(
        "Negative cofactor is incorrect.",
        expectedNegativeCofactor,
        actualNegativeCofactor
    );
    assertEquals(
        "Positive cofactor is incorrect.",
        expectedPositiveCofactor,
        actualPositiveCofactor
    );
  }
}