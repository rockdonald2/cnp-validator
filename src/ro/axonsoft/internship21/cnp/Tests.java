package ro.axonsoft.internship21.cnp;

import org.junit.Assert;
import org.junit.Test;

class Tests {

    private final static CnpValidatorImpl m_test_validator = new CnpValidatorImpl();

    public static class TestBirthCounty {

        @Test
        public void case1() {
            try {
                Assert.assertEquals("BU", m_test_validator.getBirthCounty("46").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                Assert.assertEquals("CL", m_test_validator.getBirthCounty("51").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertEquals("AB", m_test_validator.getBirthCounty("01").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertEquals("AB", m_test_validator.getBirthCounty(" 1").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertEquals("DJ", m_test_validator.getBirthCounty("16").getAbrv());
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getBirthCounty(""));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getBirthCounty("a"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getBirthCounty("?"));
        }

    }

    public static class TestBirthDate {

        @Test
        public void case1() {
            try {
                final var expected = new CalDateImpl("2000-7-22");
                Assert.assertEquals(expected.toString(), m_test_validator.getBirthDate("000722", "5").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                final var expected = new CalDateImpl("2000-5-22");
                Assert.assertEquals(expected.toString(), m_test_validator.getBirthDate("000522", "6").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                final var expected = new CalDateImpl("1911-5-22");
                Assert.assertEquals(expected.toString(), m_test_validator.getBirthDate("110522", "2").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                final var expected = new CalDateImpl("2011-11-1");
                Assert.assertEquals(expected.toString(), m_test_validator.getBirthDate("111101", "9").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                final var expected = new CalDateImpl("1899-12-12");
                Assert.assertEquals(expected.toString(), m_test_validator.getBirthDate("991212", "3").toString());
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getBirthDate("", ""));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getBirthDate("xxxxxxxxxx", ""));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getBirthDate("99x212", "3"));
        }

    }

    public static class TestCentury {

        @Test
        public void case1() {
            try {
                Assert.assertEquals("20", m_test_validator.getCentury("5"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                Assert.assertEquals("20", m_test_validator.getCentury("6"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertEquals("19", m_test_validator.getCentury("1"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertEquals("18", m_test_validator.getCentury("4"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertEquals("", m_test_validator.getCentury("9"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getForeignStatus("n"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getForeignStatus("."));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getForeignStatus("0"));
        }

    }

    public static class TestControlNumber {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateControlNumber("190122231a102"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateControlNumber("190122??31102"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateControlNumber(""));
        }

        @Test
        public void caseThatThrows4() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateControlNumber("6010919238862"));
        }

        @Test
        public void caseThatThrows5() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateControlNumber("6080809255522"));
        }

        @Test
        public void caseThatThrows6() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateControlNumber("6090809255522"));
        }

        @Test
        public void casesThatWontThrow() {
            var exception = Assert.assertThrows(CnpException.class, () -> {
                m_test_validator.validateControlNumber("6180328015611");
                m_test_validator.validateControlNumber("1611109181231");
                m_test_validator.validateControlNumber("2740903216431");

                throw new CnpException("Didn't throw", CnpException.ErrorCode.INVALID_CNP);
            });

            Assert.assertTrue(exception.getMessage().contains("Didn't throw"));
        }

    }

    public static class TestForeignStatus {

        @Test
        public void case1() {
            try {
                Assert.assertTrue(m_test_validator.getForeignStatus("7"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case2() {
            try {
                Assert.assertTrue(m_test_validator.getForeignStatus("8"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertTrue(m_test_validator.getForeignStatus("9"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertFalse(m_test_validator.getForeignStatus("2"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertFalse(m_test_validator.getForeignStatus("4"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getForeignStatus("a"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getForeignStatus("?"));
        }

    }

    public static class TestSex {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getSex("0"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.getSex("?"));
        }

        @Test
        public void case2() {
            try {
                Assert.assertEquals(Sex.F, m_test_validator.getSex("2"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case3() {
            try {
                Assert.assertEquals(Sex.M, m_test_validator.getSex("1"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case4() {
            try {
                Assert.assertEquals(Sex.M, m_test_validator.getSex("5"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case5() {
            try {
                Assert.assertEquals(Sex.M, m_test_validator.getSex("7"));
            } catch (CnpException ignored) {}
        }

        @Test
        public void case6() {
            try {
                Assert.assertEquals(Sex.U, m_test_validator.getSex("9"));
            } catch (CnpException ignored) {}
        }

    }

    public static class TestValidateFormat {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateFormat("190122231a102"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateFormat("19012A231a102"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateFormat("190B222311102"));
        }

        @Test
        public void caseThatThrows4() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateFormat("1F01222{1a102"));
        }

        @Test
        public void caseThatThrows5() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateFormat("1F01222{1a102"));
        }

        @Test
        public void caseThatThrows6() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateFormat("189c902023((8"));
        }

        @Test
        public void casesThatWontThrow() {
            var exception = Assert.assertThrows(CnpException.class, () -> {
                m_test_validator.validateFormat("1651208211659");
                m_test_validator.validateFormat("2901103243809");
                m_test_validator.validateFormat("1610322161751");

                throw new CnpException("Didn't throw", CnpException.ErrorCode.INVALID_CNP);
            });

            Assert.assertTrue(exception.getMessage().contains("Didn't throw"));
        }

    }

    public static class TestValidateLength {

        @Test
        public void caseThatThrows1() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateLength("161032161751"));
        }

        @Test
        public void caseThatThrows2() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateLength("1"));
        }

        @Test
        public void caseThatThrows3() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateLength(""));
        }

        @Test
        public void caseThatThrows4() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateLength("4"));
        }

        @Test
        public void caseThatThrows5() {
            Assert.assertThrows(CnpException.class, () -> m_test_validator.validateLength("413212312321312312313231"));
        }

        @Test
        public void casesThatWontThrow() {
            var exception = Assert.assertThrows(CnpException.class, () -> {
                m_test_validator.validateLength("2710531138910");
                m_test_validator.validateLength("2650308082315");
                m_test_validator.validateLength("1740225335436");

                throw new CnpException("Didn't throw", CnpException.ErrorCode.INVALID_CNP);
            });

            Assert.assertTrue(exception.getMessage().contains("Didn't throw"));
        }

    }

}