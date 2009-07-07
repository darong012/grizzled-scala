/*---------------------------------------------------------------------------*\
  This software is released under a BSD-style license:

  Copyright (c) 2009 Brian M. Clapper. All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

  1.  Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

  2.  The end-user documentation included with the redistribution, if any,
      must include the following acknowlegement:

        "This product includes software developed by Brian M. Clapper
        (bmc@clapper.org, http://www.clapper.org/bmc/). That software is
        copyright (c) 2009 Brian M. Clapper."

      Alternately, this acknowlegement may appear in the software itself,
      if wherever such third-party acknowlegements normally appear.

  3.  Neither the names "clapper.org", "The Grizzled Scala Library",
      nor any of the names of the project contributors may be used to
      endorse or promote products derived from this software without prior
      written permission. For written permission, please contact
      bmc@clapper.org.

  4.  Products derived from this software may not be called "clapper.org
      Java Utility Library", nor may "clapper.org" appear in their names
      without prior written permission of Brian M. Clapper.

  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
  NO EVENT SHALL BRIAN M. CLAPPER BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\*---------------------------------------------------------------------------*/

import org.scalatest.FunSuite
import grizzled.string._
import grizzled.string.util._
import grizzled.string.implicits._

/**
 * Tests the grizzled.string functions.
 */
class StringTest extends GrizzledFunSuite
{
    test("string to boolean conversions that should succeed")
    {
        val data = Map(
            "true"  -> true,
            "t"     -> true,
            "yes"   -> true,
            "y"     -> true,
            "1"     -> true,

            "false" -> false,
            "f"     -> false,
            "no"    -> false,
            "n"     -> false,
            "0"     -> false
        )

        for((input, expected) <- data;
            val permutations = List(input,
                                    input.capitalize,
                                    input.toUpperCase,
                                    " " + input,
                                    " " + input + " ",
                                    input + " ");
            s <- permutations)
        {
            expect(expected, "\"" + s + "\" -> " + expected.toString) 
            {
                val b: Boolean = s
                b
            }
        }
    }

    test("string to boolean conversions that should fail")
    {
        val data = List("tru", "tr", "z", "truee", "xtrue",
                        "000", "00", "111", "1a", "0z",
                        "fa", "fal", "fals", "falsee")

        for(input <- data;
            val permutations = List(input,
                                    input.capitalize,
                                    input.toUpperCase);
            s <- permutations)
        {
            intercept[IllegalArgumentException]
            {
                val b: Boolean = s
                b
            }
        }
    }

    test("tokenizing quoted strings")
    {
        val data = Map(
            "a b c"                        -> List("a", "b", "c"),
            "aa bb cc"                     -> List("aa", "bb", "cc"),
            "\"aa\\\"a\" 'b'"              -> List("aa\"a", "b"),
            "one two '3\" four'"       -> List("one", "two", "3\" four"),
            "\"a'b    c'\" 'b\\'c  d' a\"" -> List("a'b    c'", "b'c  d", "a\"")
        )

        for((input, expected) <- data)
        {
            expect(expected, "\"" + input + "\" -> " + expected.toString)
            {
                tokenizeWithQuotes(input)
            }
        }

    }

    test("ltrim")
    {
        val data = Map(
            "a b c"                        -> "a b c",
            "                     a"       -> "a",
            "                     a  "     -> "a  ",
            "                      "       -> "",
            ""                             -> ""
        )

        for((input, expected) <- data)
        {
            expect(expected, "\"" + input + "\" -> " + expected.toString)
            {
                input.ltrim
            }
        }
    }

    test("rtrim")
    {
        val data = Map(
            "a b c"                        -> "a b c",
            "a                     "       -> "a",
            "  a                     "     -> "  a",
            "                      "       -> "",
            ""                             -> ""
        )

        for((input, expected) <- data)
        {
            expect(expected, "\"" + input + "\" -> " + expected.toString)
            {
                input.rtrim
            }
        }
    }

    test("WordWrapper")
    {
        val s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "In congue tincidunt fringilla. Sed interdum nibh vitae " +
                "libero fermentum id dictum risus facilisis. Pellentesque " +
                "habitant morbi tristique senectus et netus et malesuada " +
                "fames ac turpis egestas. Sed ante nisi, pharetra ut " +
                "eleifend vitae, congue ut quam. Vestibulum ante ipsum " +
                "primis in."

        val data = Map(
            (s, 79, 0, "", ' ') ->
"""Lorem ipsum dolor sit amet, consectetur adipiscing elit. In congue tincidunt
fringilla. Sed interdum nibh vitae libero fermentum id dictum risus facilisis.
Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac
turpis egestas. Sed ante nisi, pharetra ut eleifend vitae, congue ut quam.
Vestibulum ante ipsum primis in.""",

            (s, 40, 0, "", ' ') ->
"""Lorem ipsum dolor sit amet, consectetur
adipiscing elit. In congue tincidunt
fringilla. Sed interdum nibh vitae
libero fermentum id dictum risus
facilisis. Pellentesque habitant morbi
tristique senectus et netus et malesuada
fames ac turpis egestas. Sed ante nisi,
pharetra ut eleifend vitae, congue ut
quam. Vestibulum ante ipsum primis in.""",

            (s, 40, 5, "", ' ') ->
"""     Lorem ipsum dolor sit amet,
     consectetur adipiscing elit. In
     congue tincidunt fringilla. Sed
     interdum nibh vitae libero
     fermentum id dictum risus
     facilisis. Pellentesque habitant
     morbi tristique senectus et netus
     et malesuada fames ac turpis
     egestas. Sed ante nisi, pharetra ut
     eleifend vitae, congue ut quam.
     Vestibulum ante ipsum primis in.""",

            (s, 60, 0, "foobar: ", ' ') ->
"""foobar: Lorem ipsum dolor sit amet, consectetur adipiscing
        elit. In congue tincidunt fringilla. Sed interdum
        nibh vitae libero fermentum id dictum risus
        facilisis. Pellentesque habitant morbi tristique
        senectus et netus et malesuada fames ac turpis
        egestas. Sed ante nisi, pharetra ut eleifend vitae,
        congue ut quam. Vestibulum ante ipsum primis in.""",

            (s, 60, 0, "foobar: ", '.') ->
"""foobar: Lorem ipsum dolor sit amet, consectetur adipiscing
........elit. In congue tincidunt fringilla. Sed interdum
........nibh vitae libero fermentum id dictum risus
........facilisis. Pellentesque habitant morbi tristique
........senectus et netus et malesuada fames ac turpis
........egestas. Sed ante nisi, pharetra ut eleifend vitae,
........congue ut quam. Vestibulum ante ipsum primis in."""

        )

        for((input, expected) <- data)
        {
            val (string, width, indent, prefix, indentChar) = input

            expect(expected, "\"" + input + "\" -> " + expected.toString)
            {
                val wrapper = new WordWrapper(width, indent, prefix, indentChar)
                wrapper.wrap(string)
            }
        }
    }
}