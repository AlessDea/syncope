package org.apache.syncope.core.provisioning.api.utils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(value= Parameterized.class)
public class RealmUtilsTest {

    private enum Validity{
        VALID, INVALID, EMPTY, NULL
    }


    private boolean expected;
    private String newRealm;
    private Validity realms;


    static Set<String> valid_set;
    static Set<String> empty_set;

    private static String newR1 = "/a/c";
    private static String newR2 = "/b";
    private static String newR3 = "/b/c";



    @BeforeClass
    public static void configure(){
        valid_set = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        empty_set = new HashSet<>();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){

        return Arrays.asList(new Object[][]{
                //exp       realms          newRealm
                {true, Validity.VALID, newR1},
                {true, Validity.VALID, newR2},
                {false, Validity.VALID, newR3},
                {false, Validity.VALID, null},
                {false, Validity.VALID, ""},

                {true, Validity.EMPTY, newR1},
                {false, Validity.EMPTY, null},
                {false, Validity.EMPTY, ""},
        });
    }


    public RealmUtilsTest(boolean expected, Validity realms, String newRealm) {
        this.expected = expected;
        this.realms = realms;
        this.newRealm = newRealm;

    }

    @Test
    public void testRealmUtils(){

        boolean res;
        Set<String> realmss;


        if(this.realms.equals(Validity.VALID))
            realmss = valid_set;
        else
            realmss = empty_set;

        System.out.println(this.newRealm + " " + realmss + " --> expected: " + this.expected);
        try {
            res = RealmUtils.normalizingAddTo(realmss, this.newRealm);
        } catch (Exception e) {
            //e.printStackTrace();
            res = false;
        }

        assertEquals(this.expected, res);

    }
}


