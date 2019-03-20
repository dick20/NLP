# encoding=utf-8

# 加载字典
def load_word_list():
	max_length = 0
	word_dict = set()
	for line in open('./data/corpus.dict.txt',encoding='utf-8',errors='ignore').readlines():
		tmp = len(line)
		if(max_length < tmp):
			max_length = tmp
		word_dict.add(line.strip())
	return {
	 		'max_length':max_length,
	 		'word_dict':word_dict
	 		}


# 最大正向匹配
def max_left_match(line):
	pass

# 测试
def main():
	for line in open('./data/corpus.sentence.txt',encoding='utf-8',errors='ignore').readlines():
		# 去标点
		# 最大匹配
		# 结果
