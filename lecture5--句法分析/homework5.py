import nltk

# 使用nltk工具对以下句子的词性标注
text = nltk.word_tokenize('the lawyer questioned the witness about the revolver')
str = nltk.pos_tag(text)
print("Answer 1 : ")
print(str)
print("\n------------------------------------------------\n")
print("Answer 2 : ")
# 测试答案
grammar = nltk.CFG.fromstring("""
S -> NP VP
VP -> VBD NP | VBD NP PP
PP -> IN NP
NP -> DT NN | DT NN PP
DT -> "the" | "a" 
NN -> "boy" | "dog" | "rod" 
VBD -> "saw"
IN -> "with"
""")
words = nltk.word_tokenize('the boy saw the dog with a rod')
tags = nltk.pos_tag(words)
rd_parser = nltk.RecursiveDescentParser(grammar)
for tree in rd_parser.parse(words):
	print()
	print(tree)