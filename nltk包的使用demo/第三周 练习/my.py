# coding:utf-8
import nltk
import re
import string
from nltk.tokenize import sent_tokenize 
from nltk.corpus import stopwords
from nltk.stem import LancasterStemmer
from nltk.probability import FreqDist


# 标点符号过滤
def filter_punctuation(words):
    new_words = [];
    illegal_char = string.punctuation + '【·！…（）—：“”？《》、；】' 
    pattern=re.compile('[%s]' % re.escape(illegal_char))
    for word in words:
        new_word = pattern.sub(u'', word)
        if not new_word == u'':
            new_words.append(new_word)
    return new_words

# 处理停止词
def filter_stop_words(words):
    stops=set(stopwords.words('english'))
    words = [word for word in words if word.lower() not in stops]
    return words

# 分词、提取词干
def Word_segmentation_and_extraction(text):
    words=nltk.word_tokenize(text)
    stemmerlan=LancasterStemmer()
    for i in range(len(words)):
        words[i] = stemmerlan.stem(words[i])
    return words

# 低频词过滤
def filter_low_frequency_words(words):
    fdist = FreqDist(words)
    return fdist

text_en = open(u'./data/text_en.txt',encoding='utf-8',errors='ignore').read()

# 分词、提取词干
f1 = open("1.txt", "w",encoding='utf-8')
words_seg=Word_segmentation_and_extraction(text_en)

for word in words_seg:
    f1.write(word+'\n')

# 去除停用词
f2 = open("2.txt", "w",encoding='utf-8')
words_no_stop=filter_stop_words(words_seg)

for word in words_no_stop:
    f2.write(word+'\n')

# 去除标点符号
f3 = open("3.txt", "w",encoding='utf-8')
words_no_punc = filter_punctuation(words_no_stop)

for word in words_no_punc:
    f3.write(word+'\n')

# 低频词过滤 fre为20
fre = 20
f4 = open("4.txt", "w",encoding='utf-8')
fdist_no_low_fre = filter_low_frequency_words(words_no_punc)
for key in fdist_no_low_fre:
    if(fdist_no_low_fre[key] > fre):
        f4.write(key + ' ' + str(fdist_no_low_fre[key])+'\n')

# 绘制离散图，查看指定单词（Elizabeth, Darcy, Wickham, Bingley, Jane）在文中的分布位置
# 新建一个Text对象
f5 = open("5.txt", "w",encoding='utf-8')
my_text = nltk.text.Text(nltk.word_tokenize(text_en))
name = ['Elizabeth', 'Darcy', 'Wickham', 'Bingley', 'Jane']
for n in name:
    my_text.concordance(n)
my_text.dispersion_plot(name[:])

# 对前20个有意义的高频词，绘制频率分布图
n = 20
fdist = FreqDist(words_no_punc)
fdist.plot(n)
