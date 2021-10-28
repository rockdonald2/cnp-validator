package edu.cnp;

import edu.cnp.exception.*;
import org.junit.Assert;
import org.junit.Test;

class Tests {

    private final static CnpValidatorImpl test_validator = new CnpValidatorImpl();

    public static class TestBirthCounty {

        @Test
        public void case1() {
            try {
                Assert.assertEquals("BU", test_validator.getBirthCounty("6000722407864").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                Assert.assertEquals("CL", test_validator.getBirthCounty("6000722515408 ").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertEquals("AB", test_validator.getBirthCounty(" 6000722018021").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertEquals("AB", test_validator.getBirthCounty(" 2990722015531 ").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertEquals("DJ", test_validator.getBirthCounty(" 2990722168675").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(InvalidCountyException.class, () -> test_validator.getBirthCounty("2990722  8675"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(InvalidCountyException.class, () -> test_validator.getBirthCounty("2990722aa8675"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(InvalidCountyException.class, () -> test_validator.getBirthCounty("2990722??8675"));
        }

    }

    public static class TestBirthDate {

        @Test
        public void case1() {
            try {
                final var expected = new CalDateImpl("2000-7-22");
                Assert.assertEquals(expected.toString(), test_validator.getBirthDate("5000722194033").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                final var expected = new CalDateImpl("2000-5-22");
                Assert.assertEquals(expected.toString(), test_validator.getBirthDate("6000522405695").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                final var expected = new CalDateImpl("1911-5-22");
                Assert.assertEquals(expected.toString(), test_validator.getBirthDate("1110522326297").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                final var expected = new CalDateImpl("2011-11-1");
                Assert.assertEquals(expected.toString(), test_validator.getBirthDate("6111101098548").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                final var expected = new CalDateImpl("1899-12-12");
                Assert.assertEquals(expected.toString(), test_validator.getBirthDate("3991212152713").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(InvalidBirthDateException.class, () -> test_validator.getBirthDate("2991322518675"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(InvalidCenturyException.class, () -> test_validator.getBirthDate("xxxxxxxxxxxxx"));
        }

    }

    public static class TestCentury {

        @Test
        public void case1() {
            try {
                Assert.assertEquals("20", test_validator.getCentury("5"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                Assert.assertEquals("20", test_validator.getCentury("6"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertEquals("19", test_validator.getCentury("1"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertEquals("18", test_validator.getCentury("4"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertEquals("", test_validator.getCentury("9"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(InvalidCenturyException.class, () -> test_validator.getForeignStatus("n"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(InvalidCenturyException.class, () -> test_validator.getForeignStatus("."));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(InvalidCenturyException.class, () -> test_validator.getForeignStatus("0"));
        }

    }

    public static class TestControlNumber {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateControlNumber("190122231a102"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateControlNumber("190122??31102"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateControlNumber("                "));
        }

        @Test
        public void caseThatThrows4() {
            Assert.assertThrows(InvalidControlNumberException.class, () -> test_validator.validateControlNumber("6010919238862"));
        }

        @Test
        public void caseThatThrows5() {
            Assert.assertThrows(InvalidControlNumberException.class, () -> test_validator.validateControlNumber("6080809255522"));
        }

        @Test
        public void caseThatThrows6() {
            Assert.assertThrows(InvalidControlNumberException.class, () -> test_validator.validateControlNumber("6090809255522"));
        }

        @Test
        public void casesThatWontThrow() {
            var exception = Assert.assertThrows(CnpException.class, () -> {
                test_validator.validateControlNumber("6180328015611");
                test_validator.validateControlNumber("1611109181231");
                test_validator.validateControlNumber("2740903216431");

                throw new InvalidControlNumberException("Didn't throw");
            });

            Assert.assertTrue(exception.getMessage().contains("Didn't throw"));
        }

    }

    public static class TestForeignStatus {

        @Test
        public void case1() {
            try {
                Assert.assertTrue(test_validator.getForeignStatus("8990722165726"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                Assert.assertTrue(test_validator.getForeignStatus("7990722169481"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertFalse(test_validator.getForeignStatus("1990722169552"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertFalse(test_validator.getForeignStatus(" 1500722088076"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(InvalidSexException.class, () -> test_validator.getForeignStatus(" a500722088076"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(InvalidSexException.class, () -> test_validator.getForeignStatus(" ?500722088076"));
        }

    }

    public static class TestSex {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(InvalidSexException.class, () -> test_validator.getSex(" 0500722088076"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(InvalidSexException.class, () -> test_validator.getSex(" ?500722088076"));
        }

        @Test
        public void case2() {
            try {
                Assert.assertEquals(Sex.F, test_validator.getSex("2500722089125"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertEquals(Sex.M, test_validator.getSex("1500722089262"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertEquals(Sex.M, test_validator.getSex("5000722085375"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertEquals(Sex.M, test_validator.getSex("7000722086784"));
            } catch (CnpException ignored) {}
        }

    }

    public static class TestValidateFormat {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateFormat("190122231a102"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateFormat("19012A231a102"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateFormat("190B222311102"));
        }

        @Test
        public void caseThatThrows4() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateFormat("1F01222{1a102"));
        }

        @Test
        public void caseThatThrows5() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateFormat("1F01222{1a102"));
        }

        @Test
        public void caseThatThrows6() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateFormat("189c902023((8"));
        }

        @Test
        public void casesThatWontThrow() {
            var exception = Assert.assertThrows(CnpFormatException.class, () -> {
                test_validator.validateFormat("1651208211659");
                test_validator.validateFormat("2901103243809");
                test_validator.validateFormat("1610322161751");

                throw new CnpFormatException("Didn't throw");
            });

            Assert.assertTrue(exception.getMessage().contains("Didn't throw"));
        }

    }

    public static class TestValidateLength {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateLength("161032161751"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateLength("1"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateLength(""));
        }

        @Test
        public void caseThatThrows4() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateLength("4"));
        }

        @Test
        public void caseThatThrows5() {
            Assert.assertThrows(CnpFormatException.class, () -> test_validator.validateLength("413212312321312312313231"));
        }

        @Test
        public void casesThatWontThrow() {
            var exception = Assert.assertThrows(CnpFormatException.class, () -> {
                test_validator.validateLength("2710531138910");
                test_validator.validateLength("2650308082315");
                test_validator.validateLength("1740225335436");

                throw new CnpFormatException("Didn't throw");
            });

            Assert.assertTrue(exception.getMessage().contains("Didn't throw"));
        }

    }

}
