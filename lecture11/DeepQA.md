# DeepQA

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



