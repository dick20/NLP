# coding:utf-8
'''
环境：python + jupyter notebook
安装 pip install nltk （http://www.nltk.org/install.html）
'''

import nltk
import re 
import string
# 第一次运行(NTLK自带语料库默认路径下载) : 
#nltk.download()


'''测试是否成功下载'''
#from nltk.corpus import brown
#print(brown.words())
#print(len(brown.sents())) # 句子数
#print(len(brown.words())) # 单词数


'''英文文本处理'''
'''词性标注'''
# text_en = open(u'./data/text_en.txt',encoding='utf-8',errors='ignore').read()
text="Don't hesitate to ask questions. Be positive." 
# 分句1
from nltk.tokenize import sent_tokenize 
print(sent_tokenize(text))

# 分词1
words=nltk.word_tokenize(text)
print(words)
