import org.scalatest.FunSuite
import grizzled.file._

// FIXME: Need to figure out how to test in a platform-independent way.

/**
 * Tests the grizzled.file functions.
 */
class FileTest extends GrizzledFunSuite
{
    test("basename")
    {
        val data = Map(
            ""             -> "",
            "foo"          -> "foo",
            "foo/bar"      -> "bar",
            "."            -> ".",
            "../foo"       -> "foo",
            "/foo/bar/baz" -> "baz"
        )

        for((path, expected) <- data)
            expect(expected, "basename(\"" + path + "\")") { basename(path)}
    }

    test("dirname")
    {
        val data = Map(
            ""             -> "",
            "foo"          -> ".",
            "foo/bar"      -> "foo",
            "."            -> ".",
            "../foo"       -> "..",
            "/foo/bar/baz" -> "/foo/bar",
            "/foo"         -> "/",
            "/foo"         -> "/",
            "/"            -> "/",
            "////"         -> "/"
        )

        for((path, expected) <- data)
            expect(expected, "dirname(\"" + path + "\")") { dirname(path)}
    }

    test("pathsplit")
    {
        val data = Map(
            ""             -> ("", ""),
            "foo"          -> (".", "foo"),
            "foo/bar"      -> ("foo", "bar"),
            "."            -> (".", ""),
            "../foo"       -> ("..", "foo"),
            "./foo"        -> (".", "foo"),
            "/foo/bar/baz" -> ("/foo/bar", "baz"),
            "/foo"         -> ("/", "foo"),
            "/"            -> ("/",  "")
        )

        for((path, expected) <- data)
            expect(expected, "pathsplit(\"" + path + "\")") { pathsplit(path)}
    }

    test("fnmatch")
    {
        val data = Map(
            ("foo", "f*") -> true,
            ("foo", "f*o") -> true,
            ("foo", "f*b") -> false,
            ("foo", "*") -> true,
            ("foo", "*o") -> true,
            ("a.c", "*.c") -> true,
            ("abc", "[!a-r]*") -> false,
            ("radfa.c", "[!a-r]*") -> false,
            ("radfa.c", "[^a-r]*") -> false,
            ("sabc", "[!a-r]*") -> true,
            ("sabc", "[^a-r]*") -> true
        )

        for(((string, pattern), expected) <- data)
        {
            expect(expected, 
                   "fnmatch(\"" + string + "\", \"" + pattern + "\")")
            {
                fnmatch(string, pattern) 
            }
        }
    }

    test("normalizePosixPath")
    {
        val data = Map(
            "/foo/../bar/////baz" -> "/bar/baz",
            "///////foo/bar/" -> "/foo/bar",
            "." -> ".",
            "" -> ".",
            "/" -> "/",
            "//" -> "//",
            "///" -> "/",
            "//////////////////." -> "/"
        )

        for ((path, expected) <- data)
            expect(expected, "normalizePosixPath(\"" + path + "\")") 
            {
                normalizePosixPath(path)
            }
    }

    test("normalizeWindowsPath")
    {
        val data = Map(
            "\\" -> "\\",
            "c:\\foo\\" -> "c:\\foo",
            "c:\\foo" -> "c:\\foo",
            "c:\\foo\\bar" -> "c:\\foo\\bar",
            "\\\\server\\foo" -> "\\\\server\\foo",
            "\\\\server\\foo\\bar\\..\\baz" -> "\\\\server\\foo\\baz",
            "c:\\foo\\bar\\..\\baz" -> "c:\\foo\\baz",
            "\\.." -> "\\",
            "\\..\\.." -> "\\"
        )

        for ((path, expected) <- data)
            expect(expected, "normalizeWindowsPath(\"" + path + "\")") 
            {
                normalizeWindowsPath(path)
            }
    }
}