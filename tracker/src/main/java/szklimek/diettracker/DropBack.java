/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package szklimek.diettracker;

import java.util.Random;

class DropBack {

    private static final Random RANDOM = new Random();

    static int getRandomDishDrawable() {
        switch (RANDOM.nextInt(4)) {
            default:
            case 0:
                return R.drawable.user;
            case 1:
                return R.drawable.user;
            case 2:
                return R.drawable.user;
            case 3:
                return R.drawable.user;
        }
    }
    static int getRandomChildDrawable() {
        switch (RANDOM.nextInt(10)) {
            default:
            case 0:
                return R.drawable.woman;
            case 1:
                return R.drawable.boy;
            case 2:
                return R.drawable.child;
            case 3:
                return R.drawable.cut;
             case 4:
                return R.drawable.runner;
             case 5:
                return R.drawable.vege;
            case 6:
                return R.drawable.healthycooking;
            case 7:
                return R.drawable.diet;
            case 8:
                return R.drawable.fridge;
            case 9:
                return R.drawable.kit;
                    }
    }
    static int getRandomRoomDrawable() {
        switch (RANDOM.nextInt(4)) {
            default:
            case 0:
                return R.drawable.user;
            case 1:
                return R.drawable.user;
            case 2:
                return R.drawable.user;
            case 3:
                return R.drawable.user;
        }
    }    static int getRandomBedDrawable() {
        switch (RANDOM.nextInt(4)) {
            default:
            case 0:
                return R.drawable.user;
            case 1:
                return R.drawable.user;
            case 2:
                return R.drawable.user;
            case 3:
                return R.drawable.user;
        }
    }


}
