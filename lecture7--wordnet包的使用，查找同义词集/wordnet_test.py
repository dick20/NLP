import nltk
from nltk.corpus import wordnet as wn

# 获取一个同义词集的所有单词
def get_lemma(synset_name):
	synsets = wn.synsets(synset_name)
	for i in range(len(synsets)):
		l_name = synsets[i].lemma_names()
		print(synsets[i],'如下所示 : ')
		print(l_name)

# 获取同义词的定义
def get_def(synset_name):
	synsets = wn.synsets(synset_name)
	for i in range(len(synsets)):
		l_name = synsets[i].definition()
		print(synsets[i],'如下所示 : ')
		print(l_name)

# 获取同义词的例子
def get_example(synset_name):
	synsets = wn.synsets(synset_name)
	for i in range(len(synsets)):
		l_name = synsets[i].examples()
		print(synsets[i],'如下所示 : ')
		print(l_name)

# 练习1.1
list = ['dog', 'apple', 'fly']
for item in list:
	print('1. '+item+'同义词集如下\n')
	print(wn.synsets(item))
	print()
	print('2. '+item+'同义词集中的所有单词如下\n')
	get_lemma(item)
	print()
	print('3. 同义词的具体定义\n')
	get_def(item)
	print()
	print('4. 同义词的具体例子\n')
	get_example(item)
	print('\n------------------------------\n')



def get_similarity(w1, w2):
	s1 = wn.synsets(w1)
	s2 = wn.synsets(w2)
	sim_max = 0
	for s1_item in s1:
		for s2_item in s2:
			sim = s1_item.path_similarity(s2_item)
			if (sim is None):
				sim = 0
			sim_max = max(sim_max, sim)
	return sim_max

# 练习1.2
print('查看good与beautiful的语义相似度 : ')
print(get_similarity('good', 'beautiful'))
print()
print('查看good与bad的语义相似度 : ')
print(get_similarity('good', 'bad'))
print()
print('查看dog与cat的语义相似度 : ')
print(get_similarity('dog', 'cat'))
print('\n------------------------------\n')
 
  

def get_entailments(w):
	s = wn.synsets(w)
	for item in s:
		en = item.entailments()
		if len(en) > 0:
			print(item,' ：')
			print(en)


def get_antonyms(w):
	s = wn.synsets(w)
	for item in s:
		lms = item.lemmas()
		for l in lms:
			a = l.antonyms()
			if len(a) > 0:
				print(item,' ：')
				print(a)


list2 = ['walk', 'supply', 'hot']

 # 练习1.3
for w in list2:
	print(w + '的蕴含关系 ：')
	get_entailments(w)
	print()
	print(w + '的反义词 ： ')
	get_antonyms(w)
	print('\n------------------------------\n')