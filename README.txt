
Project 1 for CS421 - University of Illinois at Chicago

Name1 rsrini7(at)uic.edu
Name2 kvarke2(at)uic.edu

-------------------------------------------------------------------------
--->SETUP<---------------------------------------------------------------

Software needed : Jre 6 or above

Download the jar to your computer.

To execute 

java -jar essay.jar <filename|folder name>

-------------------------------------------------------------------------
--->INPUT<---------------------------------------------------------------
	

java -jar essay.jar <filename|folder name>

filename = location of the file on which you want to run the test
folder name = location of the folder which has the files for which you want to run the test

-------------------------------------------------------------------------
--->OUTPUT<--------------------------------------------------------------

If you use  java -jar essay.jar <filename|folder name> to run the program and if the inputs
are correct, you will get the following message on your system out.

<filename>
Number of sentences = x, number of 1a error = x, number of 1b error = x, number of 1c error = x
Scores are 
1a = [1-5]
1b = [1-5]
1c = [1-5]
3a = [1-5]
	

-------------------------------------------------------------------------
--->FILES<---------------------------------------------------------------

We store some of the rules which we use in the application in the folder named "rules". Please make sure
there is folder named "rules" is under the same directory where essay.jar is placed.

Files in rules folder are
	1b_verb_noun_agreement_rules.txt
	1c_after_verb_rules.txt
	1c_before_verb_rules.txt
	1c_verb_verb_rules.txt
	aux_verbs.txt
	noun_tags.txt
	penn_treebank_tags.txt
	verb_tags.txt
	word_order_rules
	word_order.txt


java -jar essay.jar <filename|folder name>

filename = location of the file on which you want to run the test
folder name = location of the folder which has the files for which you want to run the test

The application gives out the output on system out.
	

-------------------------------------------------------------------------
--->TECHNIQUE<-----------------------------------------------------------

First for each essay we extract each individual sentence. Sentences extraction tries to extract
individual sentence even if they are separted by period. 
Then for each grading criteria we have defined certain set of rules that should be followed by all sentences
in english language. These rules are based on tags, the word and the location of tags and word in
the given sentence. eg after to/TO it should always be followed by base form of the verb(VB).

Similarly we have rules for each of the given criteria.


-------------------------------------------------------------------------
--->TODO<----------------------------------------------------------------
	
We have not yet decided on hot to approach the semantic part of the project. As far as the 
syntactic part is concerned we believe we have covered almost all the possible cases. 

