# 基于seq-to-seq的自动问答——DeepQA

[模型Github地址 : https://github.com/Conchylicultor/DeepQA](https://github.com/Conchylicultor/DeepQA)

## 一. 使用预训练模型

1. Extract the zip file inside `DeepQA/save/`
2. Copy the preprocessed dataset from `save/model-pretrainedv2/dataset-cornell-old-lenght10-filter0-vocabSize0.pkl`to `data/samples/`.
3. Run `./main.py --modelTag pretrainedv2 --test interactive`.

按照github的流程下载好后运行后，出现错误

```
NotFoundError (see above for traceback): Restoring from checkpoint failed. This is most likely due to a Variable name or other graph key that is missing from the checkpoint. Please ensure that you have not altered the graph expected based on the checkpoint. Original error:

Tensor name "embedding_rnn_seq2seq/embedding_rnn_decoder/rnn_decoder/output_projection_wrapper/bias" not found in checkpoint files C:\Users\asus\Desktop\DeepQA-master\save\model-pretrainedv2\model.ckpt
         [[node save/RestoreV2 (defined at C:\Users\asus\Desktop\DeepQA-master\chatbot\chatbot.py:174) ]]
```

**错误原因**：安装tensorflow的版本过高

**解决方案**：pip install tensorflow==1.0.0

## 二. 自己训练模型

### 默认参数

这里我使用了GPU来对模型进行训练，首先安装一些必要的库来支持，这里要注意版本

```python
pip install tensorflow-gpu==1.0.0
```

训练代码：

```python
python main.py --device gpu
```

这是默认的模型参数效果，仍未进行调整。

**初步结果：**

![1](C:\Users\asus\Desktop\1.png)

**测试结果：**可以看出训练三十轮出来的结果并不理想，回答问题的质量不高

![2](C:\Users\asus\Desktop\2.png)

### 分析

**训练Tricks：**

+ train word embedding model with other data 用其他数据训练embedding模型
+ pretrain RNN with non-dialogue corpus  用非对话语料库预训练RNN

为什么输出结果这么多无意义的回答语句？

```
例如
I'm sorry
I'm not
No
```

这些答案看似没错，但是对于我们的QA是毫无意义的，回答过于通用，由此看出训练效果差，没有达到智能回答的程度。

**原因：**

+ generic utterances appear often in training set
  + 这些通用的话语经常出现在训练集中
+ many words are punctuation marks or pronouns, making context RNN difficult to learn topics/concepts 
  + 许多单词都是标点符号或代词，使context RNN变得难以学习主题或概念。
+ Injections to context RNN is from encoder outputs which largely encode local structure of a sentence, making context RNN difficult to capture structures of whole sentences
  + context RNN的注入来自编码器输出，编码器输出主要对句子的局部结构进行编码，使得context RNN难以捕获整个句子的结构。

**解决办法：**

+ 增加训练次数，降低loss

+ 更多的更好的训练集
+ 使用注意力机制attention，使得context RNN能捕获整个句子的意思，而不是局限于句子的局部。

### 调参过程

可调参数有很多，可以看下图

![3](C:\Users\asus\Desktop\3.png)



#### 增加隐层cell的数量以及加深层数

+ hiddenSize = 512
+ numLayers = 3

**测试结果：**QA比之前更加具有智能，基本可以回答问题。loss与Perplexity继续下降

![6](C:\Users\asus\Desktop\6.png)

![5](C:\Users\asus\Desktop\5.png)

#### 增加训练的epoch

epoch = 40

#### 调低学习率

在训练后期，变化变小，此时降低学习率有利于模型继续收敛。

lr = 0.0005

#### 增加embedding_size

`embedding_size`:向量值太简单了，没有相关性，无法直接用于计算卷积神经网络，所以在训练模型过程中用embedding算法得出每个向量值在多个维度的语义和相关性数据。

embedding_size = 128

**最终结果：**

loss**在1.80\~1.90之间** 

Perplexity可以控制**在6~7之间**

![7](C:\Users\asus\Desktop\7.png)

QA运行结果：**具有一定的智能**

![8](C:\Users\asus\Desktop\8.png)