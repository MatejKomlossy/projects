module Formula.Parser exposing (parse, parseSigned, parseTerm, parseSubstitution)

{-| This library parses formulas.


# Parsers

@docs parse, parseSigned, parseTerm, parseSubstitution

-}

import Dict exposing (Dict)
import Formula exposing (Formula(..))
import Formula.Signed exposing (Signed(..))
import Parser
    exposing
        ( (|.)
        , (|=)
        , Parser
        , Trailing(..)
        , backtrackable
        , chompWhile
        , end
        , float
        , keyword
        , lazy
        , map
        , oneOf
        , sequence
        , succeed
        , symbol
        , variable
        )
import Set exposing (Set)
import Term exposing (Term(..))
import Unicode


{-| Parse string to Signed Formula
-}
parseSigned : String -> Result (List Parser.DeadEnd) (Signed Formula)
parseSigned =
    Parser.run (succeed identity |. spaces |= signedFormula |. spaces |. end)


{-| Parses string to Term
-}
parseTerm : String -> Result (List Parser.DeadEnd) Term
parseTerm =
    Parser.run (succeed identity |. spaces |= term |. spaces |. end)


{-| Parse string to Formula
-}
parse : String -> Result (List Parser.DeadEnd) Formula
parse =
    Parser.run (succeed identity |. spaces |= formula |. spaces |. end)


{-| Parse string to Substitution
-}
parseSubstitution : String -> Result (List Parser.DeadEnd) Term.Substitution
parseSubstitution str =
    Parser.run (succeed identity |. spaces |= substitution |. spaces |. end) ("{" ++ str ++ "}")


{-| Format parsing error
-}
errorString : List Parser.DeadEnd -> String
errorString e =
    "Invalid formula: " ++ Parser.deadEndsToString e


signedFormula : Parser (Signed Formula)
signedFormula =
    succeed identity
        |. spaces
        |= oneOf
            [ succeed T
                |. keyword "T"
                |. spaces
                |= formula
            , succeed F
                |. keyword "F"
                |. spaces
                |= formula
            ]


formula : Parser Formula
formula =
    oneOf
        [ backtrackable <| eq
        , backtrackable <| negEq
        , succeed PredAtom
            |= identifier
            |. spaces
            |= oneOf
                [ args
                , succeed []
                ]
        , lazy (\_ -> quantified [ "???", "\\A", "\\forall", "\\a" ] ForAll)

        -- keep \exists before \e
        , lazy (\_ -> quantified [ "???", "\\E", "\\exists", "\\e" ] Exists)
        , succeed Neg
            |. oneOfSymbols [ "-", "??", "~" ]
            |. spaces
            |= lazy (\_ -> formula)
        , backtrackable <| lazy (\_ -> binary [ "&", "???", "/\\" ] Conj)
        , backtrackable <| lazy (\_ -> binary [ "|", "???", "\\/" ] Disj)
        , backtrackable <| lazy (\_ -> binary [ "->", "???" ] Impl)
        , backtrackable <| lazy (\_ -> binary [ "<->", "???" ] Equiv)
        , succeed identity
            |. symbol "("
            |. spaces
            |= lazy (\_ -> formula)
            |. spaces
            |. symbol ")"
        ]


binary : List String -> (Formula -> Formula -> value) -> Parser value
binary conn constructor =
    succeed constructor
        |. symbol "("
        |. spaces
        |= lazy (\_ -> formula)
        |. spaces
        |. oneOfSymbols conn
        |. spaces
        |= lazy (\_ -> formula)
        |. spaces
        |. symbol ")"


quantified : List String -> (String -> Formula -> Formula) -> Parser Formula
quantified symbols constructor =
    succeed constructor
        |. oneOfSymbols symbols
        |. spaces
        |= lazy (\_ -> identifier)
        |. spaces
        |= lazy (\_ -> formula)


eq : Parser Formula
eq =
    succeed EqAtom
        |= term
        |. spaces
        |. oneOfSymbols [ "???", "=" ]
        |. spaces
        |= term


negEqAtom : Term -> Term -> Formula
negEqAtom a b =
    Neg (EqAtom a b)


negEq : Parser Formula
negEq =
    succeed negEqAtom
        |= term
        |. spaces
        |. oneOfSymbols [ "!=", "/=", "???" ]
        |. spaces
        |= term


args : Parser (List Term)
args =
    sequence
        { start = "("
        , separator = ","
        , end = ")"
        , spaces = spaces
        , item = term
        , trailing = Forbidden
        }


term : Parser Term
term =
    identifier
        |> Parser.andThen
            (\name ->
                oneOf
                    [ succeed (\fargs -> Fun name fargs)
                        |= lazy (\_ -> args)
                    , succeed (Var name)
                    ]
            )


substitution : Parser Term.Substitution
substitution =
    map Dict.fromList <|
        Parser.sequence
            { start = "{"
            , separator = ","
            , end = "}"
            , spaces = spaces
            , item = substPair
            , trailing = Forbidden
            }


substPair : Parser ( String, Term )
substPair =
    succeed Tuple.pair
        |= identifier
        |. spaces
        |. oneOfSymbols [ "->", "???", "???" ]
        |. spaces
        |= term


identifier : Parser String
identifier =
    variable
        { start = isIdentChar
        , inner = isIdentChar
        , reserved = Set.empty
        }


oneOfSymbols : List String -> Parser ()
oneOfSymbols syms =
    oneOf (List.map symbol syms)


isIdentChar : Char -> Bool
isIdentChar char =
    Unicode.isAlphaNum char
        || char
        == '_'
        || char
        == '$'


spaces : Parser ()
spaces =
    chompWhile (\c -> c == ' ' || c == '\t' || c == '\n' || c == '\u{000D}')
