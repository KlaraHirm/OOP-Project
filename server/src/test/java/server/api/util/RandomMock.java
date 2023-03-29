package server.api.util;

import java.util.Random;

public class RandomMock extends Random {

        private int nextInt = 0;

        public boolean wasCalled = false;

        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }

        public void setNextInt(int val) {
            nextInt = val;
        }
    }