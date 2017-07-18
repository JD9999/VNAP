/*
 * Copyright 2017 Alvaro Stagg [alvarostagg@protonmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package VNAP;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Hash {

    private String hashedPassword = new String();
    private MessageDigest md;

    public Hash(String password) {
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(), 0, password.length());
            hashedPassword = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
