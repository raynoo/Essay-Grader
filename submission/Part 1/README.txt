
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

Files in rules folder are:
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

First for each essay we extract each individual sentence. For extracting individual sentences
we have a complex set of rules. For example, if sentences are separated by period, 
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

-------------------------------------------------------------------------
--->TODO<----------------------------------------------------------------
	
We have not yet decided on how to approach the semantic part of the project. As far as the 
syntactic part is concerned we believe we have covered almost all the possible cases. 

