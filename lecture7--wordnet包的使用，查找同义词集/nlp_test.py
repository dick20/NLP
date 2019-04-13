import spacy
nlp = spacy.load('en_core_web_sm')

# Add neural coref to SpaCy's pipe
import neuralcoref
neuralcoref.add_to_pipe(nlp)

l = ['My sister has a dog. She loves him.', 
'Some like to play football, others are fond of basketball.',
'The more a man knows, the more he feels his ignorance']

for item in l:
	doc = nlp(item)
	flag = doc._.has_coref
	print('\n' + item + ' 如下所示')
	print('判断是否存在共指指代 ： ')
	print(flag)
	print('共指链 ： ')
	if flag:
		print(doc._.coref_clusters)