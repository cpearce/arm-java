// Copyright 2018 Chris Pearce
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package nz.org.pearce.arm;

import java.util.Arrays;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testIntSetsUnion() {
        int[][][] testCases = {
            {{1,2,3}, {4,5,6}, {1,2,3,4,5,6}},
            {{1,2,3,4,5}, {4,5,6}, {1,2,3,4,5,6}},
            {{1}, {4,5,6}, {1,4,5,6}},
            {{}, {}, {}},
            {{1}, {}, {1}},
            {{}, {1}, {1}},
            {{1,2,3}, {1,2,3}, {1,2,3}},
        };
        for (int[][] test: testCases) {
            assertTrue(Arrays.equals(IntSets.union(test[0], test[1]), test[2]));
        }
    }

    public void testIntSetsIntersection() {
        int[][][] testCases = {
            {{1,2,3}, {4,5,6}, {}},
            {{1,2,3,4,5}, {4,5,6}, {4,5}},
            {{1}, {4,5,6}, {}},
            {{}, {}, {}},
            {{1}, {}, {}},
            {{}, {1}, {}},
            {{1,2,3}, {1,2,3}, {1,2,3}},
            {{1,2}, {1,2,3}, {1,2}},
        };
        for (int[][] test: testCases) {
            assertTrue(Arrays.equals(IntSets.intersection(test[0], test[1]), test[2]));
        }
    }

    public void testIntSetsSubtract() {
        int[][][] testCases = {
            {{1,2,3,4}, {1,2}, {3,4}},
            {{1,2,3}, {}, {1,2,3}},
            {{1,2,3}, {1,2,3}, {}},
            {{1,2}, {1}, {2}},
            {{1,2}, {2}, {1}},
            {{1,2,3}, {1}, {2,3}},
            {{1,2,3}, {2}, {1,3}},
            {{1,2,3}, {3}, {1,2}},
            {{1}, {1}, {}},
            {{}, {0}, {}},
        };
        for (int[][] test: testCases) {
            assertTrue(Arrays.equals(IntSets.subtract(test[0], test[1]), test[2]));
        }
    }

    public void testIntSetsCompare() {
        assertTrue(IntSets.compare(new int[]{1,3,4}, new int[]{1,3,5}) < 0);
        assertTrue(IntSets.compare(new int[]{1,3,4}, new int[]{1,3,3}) > 0);
    }

}
