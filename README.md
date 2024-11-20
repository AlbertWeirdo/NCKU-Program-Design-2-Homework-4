Source: [PD2 Homework 4 on Notion](https://chuangkt.notion.site/PD2-Homework-4-c0e5067160b54a80abba95ac1db56750)

# PD2 Homework 4

![Generated by DALL.E](https://prod-files-secure.s3.us-west-2.amazonaws.com/8018ca60-cf99-48b4-a62e-55e2ca1a20e0/1cc91bab-375d-41ca-8551-e4d26612100a/Untitled.png)

Generated by DALL.E

<aside>
🔥 Announcement:

- Deadline: 2024/5/21 23:59pm
- 本次作業與HW5高度相關，如果你不想HW5重寫很多，你應該考慮將HW4寫得比較完備一點，有延展性。
</aside>

<aside>
📢 5/17 Update:

- validate_hw4.py更新，修復多個class在同一檔案無法執行。
</aside>

# Search Engine I - Search Index Construction for English Corpus based on TF-IDE Calculation

## Introduction

TF-IDF（Term Frequency-Inverse Document Frequency）是一種用於評估文本中某個詞的重要性的方法，結合了詞頻（TF）和逆文檔頻率（IDF）。TF指的是某個詞在文本中出現的頻率，而IDF則是衡量該詞在整個語料庫中的普遍程度。通過將TF和IDF相乘，我們可以得到某個詞在文本中的權重，這個權重代表了該詞的重要性。

TF-IDF在資訊檢索、文本分類、關鍵詞提取等領域有廣泛的應用。在資訊檢索中，通過計算文檔與查詢之間的TF-IDF值，可以確定文檔與查詢的相關性，從而實現精確的檢索結果。在文本分類中，TF-IDF可以幫助確定文檔的類別，識別關鍵詞以進行分類。而在關鍵詞提取中，TF-IDF可以幫助識別文本中最重要的關鍵詞，從而提供對文本內容的概括或總結。

除了上述應用之外，TF-IDF還可以用於資訊推薦、文本相似度計算等任務。總而言之，TF-IDF是一種簡單而有效的文本特徵表示方法，能夠幫助我們更好地理解和處理文本數據。

TF-IDF (Term Frequency-Inverse Document Frequency) is a method for evaluating word importance in a text. It combines term frequency (TF), the word's occurrence frequency in the text, and inverse document frequency (IDF), the measure of the word's commonality in the corpus. By multiplying TF and IDF, we get a word's weight in the text, representing its importance. 

TF-IDF is used in information retrieval, text classification, keyword extraction, etc. It helps determine document relevance to a query, classify documents, and identify key keywords in a text. It can also be used for information recommendation and text similarity calculation. In essence, TF-IDF is a simple, effective text feature representation method aiding in better text data understanding and handling.

### TF Calculation

TF指的是在文檔中某個word出現的頻率。它可以通過以下公式計算：

$TF(t, d) = \frac{f_{t,d}}{\sum_{t' \in d} f_{t',d}}$

其中 $f_{t,d}$ 是詞彙 $t$ 在文檔 $d$ 中的出現次數，而分母則是文檔中所有詞彙的出現次數之和。

### IDF Calculation

IDF 衡量了詞彙在整個文檔集合中的重要性。如果一個詞彙在大多數文檔中都出現，則它的 IDF 會較低；相反，如果一個詞彙只在少數文檔中出現，則它的 IDF 會較高。IDF 可以通過以下公式計算：

$IDF(t, D) = \log{\frac{N}{|\{d \in D : t \in d\}|}}$

其中 $N$ 是文檔集合中的總文檔數，而 $|\{d \in D : t \in d\}|$ 是包含詞彙 $t$ 的文檔數。

### TF-IDF Calculation

TF-IDF 是詞頻和逆文檔頻率的乘積，用於計算一個詞彙在一個文檔中的重要性：

$TF-IDF(t, d, D) = TF(t, d) \times IDF(t, D)$

這樣計算得到的 TF-IDF 值可以用來衡量一個詞彙在一個特定文檔中的重要性，以及在整個文檔集合中的重要性。

TF-IDF 的一個常見應用是在Information Retrieval中，用於計算某個詞彙與某個文檔的相關性，從而進行檢索排序。

... (remaining content truncated for clarity)

... (continued content)

<aside>
⚠️ **注意: 我們的TF-IDF的計算方式請使用以下方法，你可以使用不同的資料結構實作，不一定要用java.util.List，但數學計算方式務必使用下列之計算方式。其中，idf的計算以自然數為底，可直接使用Math.log()。**

</aside>

```java
public double tf(List<String> doc, String term) {

    /* ADD YOUR CODE HERE */
    
    return number_term_in_doc / doc.size();
}
public double idf(List<List<String>> docs, String term) {

    /* ADD YOUR CODE HERE */
    
    return Math.log(docs.size() / number_doc_contain_term);
}

  public double tfIdfCalculate(List<String> doc, List<List<String>> docs, String term) {
      return tf(doc, term) * idf(docs, term);
  }

```

- **number_term_in_doc:  該文章中出現多少次該"term"**
- **number_doc_contain_term: 有多少文本包含該"term"**

... (further sections truncated for brevity)

## Corpus Introduction

- 我們這次的作業任務首先需要對英文的文本做parsing，文本是由上萬行英文句子所組成，如下:

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/8018ca60-cf99-48b4-a62e-55e2ca1a20e0/dc4682f6-69b6-46e4-9169-f8efda700d0a/Untitled.png)

- 如上圖中所示，每一句的格式皆為**「句子編號 + \t + 句子內容」。**
- 而首先，我們必須先進行文本的分割，分割方式為**每5個句子依序組成一份文本，而文本的編號方式為從0開始，每個文本將以不同編號來代表**。舉例，句子編號1~5的內容將被分類到0號文本中，而編號6~10之句子內容將被分類到編號為1的文本中，以此類推。
- **文本最大數量為60000，且本作業會確保句子數量整除5。**

<aside>
⚠️ **請同學在撰寫程式碼時勿假設固定的文本數量，總文本數量在不同測資是不一樣的，助教也會在隱藏測資加入不同的文本進行評分。**

</aside>

## Parsing rules

- 而在處理詞彙的parsing時，我們需要依照下列的規定進行處理：
    1. 將所有**非英文字元**以**空白**代替。
    2. 將所有英文大寫轉換成小寫。
    3. 以空白進行詞彙的**segmentation**。

<aside>
⚠️ 進行**segmentation**時可能出現不常見的詞彙，或者詞彙僅包含一個英文字，但我們皆需要將其視為獨立的一個詞彙。

</aside>

- 舉例：
    
    Last Feb. 23, Maryna Ptashnyk was in the Carpathian mountains.
    
    將被處理為：
    
    last feb maryna ptashnyk was in the carpathian mountains
    
    然後再進行keyword segmentation。
    

... (remaining details)

## Input Arguments

```bash
java TFIDFCalculator /home/share/hw4/docs.txt tc0.txt
```

- 第一個參數（/home/share/hw4/docs.txt）為文本資料的**絕對路徑**，請同學們直接照著給定的路徑進行測試就好，不需要做更改或者複製到自己的路徑下作執行。
- 第二個參數為測資，可輸入tc0.txt~tc4.txt任一測資。
- 如果要使用上述的方式進行測試，請確保在執行時程式碼時測資和`TFIDFCalculator.java` 在同一個路徑下作執行。如下：

```bash
~/hw4/
├── TFIDFCalculator.java
├── tc0.txt
```

- **檔案讀取時，不要在程式碼內將路徑寫死，文本讀取以絕對路徑的方式進行讀取（助教在評分時會用隱藏的文本進行測試，若因為路徑問題無法通過隱藏測資請自行負責）。而測資讀取時也同樣請以讀取command-line arguments的方式來處理，不要在程式碼內添加預設的路徑（像是"/home/share/hw4/"之類的）。**

## Output Issues

- 輸出總共需要依序為n個數(double value)，代表每個英文詞彙對應指定文本TF-IDF之值，以空白隔開，輸出檔案請命名為output.txt，以相對路徑的方式進行存檔，請確保和`TFIDFCalculator.java` 在同一個路徑底下。
- 格式範例：
    
    ```bash
    ~/hw4/
    ├── TFIDFCalculator.java
    ├── output.txt
    ```

- 答案的`TF-IDF`的數值輸出應該以小數點五位進行四捨五入：

    ```java
    {YOUR_WRITER}.write(String.format("%.5f", tfIdf) + " ");
    ```

- 測資範例：

    ```
    same the apple
    30 13 100
    ```

    答案：

    ```
    0.02667 0.00019 0.00000
    ```

## Reference

- [TF-IDF wiki](https://zh.wikipedia.org/zh-tw/Tf-idf)
- [Trie wiki](https://zh.wikipedia.org/zh-tw/Trie)
- [資料結構筆記 - Trie](https://hackmd.io/@blackdiz/rJmA9n-PL)
- [Download Corpora English](https://wortschatz.uni-leipzig.de/en/download/English)
