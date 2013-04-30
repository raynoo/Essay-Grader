
Project 1 for CS421 - University of Illinois at Chicago

Renu Srinivasan - rsrini7(at)uic.edu
Kevin Varkey - kvarke2(at)uic.edu

-------------------------------------------------------------------------
--->SETUP<---------------------------------------------------------------


Software needed : Jre 6 or above

Download the jar to your computer. To execute the application, 
run the following command,

java -jar essay.jar <filename|folder name>


-------------------------------------------------------------------------
--->INPUT<---------------------------------------------------------------


java -jar essay.jar <filename|folder name>

filename = location of the file on which you want to run the test
folder name = location of the folder which has the files for which you want to run the test


-------------------------------------------------------------------------
--->OUTPUT<--------------------------------------------------------------


If you use "java -jar essay.jar <filename|folder name>" to run the program, and if the inputs
are correct, an "output.txt" file is generated in the current folder. This file has the grades
for each input file, such as,

1a,1b,1c,1d,2a,2b,3a,final
5,5,5,5,5,5,5,5.0
...

The entries are sorted by filename.


Additionally, the following messages are printed on to system out:

<filename>
Scores are:
1a = [1-5]
1b = [1-5]
1c = [1-5]
1d = [1-5]
2a = [1-5]
2b = [1-5]
3a = [1-5]
Final Grade = [1-5]


-------------------------------------------------------------------------
--->FILES<---------------------------------------------------------------


We store some of the rules which we use in the application in the folder named "rules". Please make sure
there is folder named "rules" is under the same directory where essay.jar is placed.

Files in rules folder are:
	1b_verb_noun_agreement_rules.txt
	1c_verb_verb_rules.txt
	1d_after_verb_rules.txt
	1d_before_verb_rules.txt
	aux_verbs.txt
	Female.txt
	Male.txt
	NeutralGender.txt
	noun_tags.txt
	penn_treebank_tags.txt
	relevent_words.txt
	verb_tags.txt
	word_order.txt
	word_order_rules


The grades calculated by the application are written to a file "output.txt" which
will be generated in the main project folder. The format of the contents are as per
the guidelines provided earlier (and is mentioned under Output section as well).
	

-------------------------------------------------------------------------
--->TECHNIQUE<-----------------------------------------------------------


Part 1:

First for each essay we extract each individual sentence. For extracting individual sentences
we have a complex set of rules. For example, if sentences are separated by a period, 
for sentences that have acronyms (eg U.S.), sentences that are missing a period and 
sentences that are joined together. We also have complex rules to handle words that start with
verb, adverbs etc.
 
After extracting sentences we then use dependencies and tagging to find verb-noun
association. We use information extracted from dependency tree e.g. we search for
tags like nsubj,pobj,iobj,cop,aux,auxsubj,dobj. From such dependency we get subjects,
verbs and objects. And then we check verb noun agreement. We also have rules that kicks 
in when there are no proper information to be extracted from the dependency tree. e.g. If a
sentence has singular proper noun (NNP) like John, it can have a verb that is singular present
third person (VBZ) such as shakes.

Similarly we have similar rules for verb verb agreement and to find missing verbs. For example
A sentence with a singular present third person verb such as is will follow a gerund verb such as
having. We also have specific cases to take care of missing verbs.


Part 2:

For criteria of correct sentence formation (1d) we have a complex set of rules to detect word order anomalies
and sentence formation anomalies. The rules cover cases such as, a TO (the word to) can only be 
followed by a VB (verb base form); a sentence should not begin with a VBZ; if a sentence starts with an
adverb then it should be followed by a comma, etc.

For criteria of text and topic coherence (2a and 2b), we implemented a method that resolves pronouns
to its corresponding nouns and another method to make sure the essay speaks about the given topic. There
is a list of words pertaining to given topic as one way to achieve this. We also check whether nearby 
sentences make sense together.


-------------------------------------------------------------------------
--->TODO<----------------------------------------------------------------


Given the project requirements and guidelines, we believe we have covered almost all 
the possible cases. Based on our performance in the class competition, we will look 
towards improving the efficiency of our application.

