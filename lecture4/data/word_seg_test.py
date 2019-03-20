# encoding=utf-8
import nltk
import string
import re
import jieba

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

# 去标点
def filter_punctuation(line):
    # 去除标点符号
    punc = "[！？。｡＂＃＄％＆＇（）＊＋，－／：；＜＝＞＠［＼］＾＿｀｛｜｝～｟｠｢｣､、〃《》「」『』【】〔〕〖〗〘〙〚〛〜〝〞〟〰〾〿–—‘’‛“”„‟…‧﹏.!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~]+"
    line = re.sub(punc, "",line)
    return line

# 最大正向匹配
def max_left_match(line, dict):
    input_str = line
    output_str = ""
    # 最大词长
    max_length = dict['max_length']
    word_dict = dict['word_dict']
    while input_str.strip() != '':
        num = max_length
        W = input_str[0:num]
        while W not in word_dict:
            num -= 1
            W = W[0:num]
            if len(W) == 1:
                break
        output_str += W + "/"
        input_str = input_str[len(W):]
    return output_str

# 利用jieba库的分词功能
def jieba_cut(line):
    line_seg = " ".join(jieba.cut(line))
    return line_seg

# 测试
def main():
    dict = load_word_list()
    for line in open('./data/corpus.sentence.txt',encoding='utf-8',errors='ignore').readlines():
        # 去标点
        new_line = filter_punctuation(line)
        # 自己写的最大匹配
        result = max_left_match(new_line, dict)
        # jieba库的分词
        #result = jieba_cut(new_line)
        # 结果
        print(result)

if __name__ == '__main__':
    main()
    # print(__name__)