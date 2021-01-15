

# 1. HashMap总览

## 1.1 hashmap底层储存结构图解

底层结构其实就是数组+链表+红黑树

![1601357648567](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201003110314-282089.png)





## 1.2 HashMap类定义

先来看看HashMap的定义：

```java
public class HashMap<K,V> extends AbstractMap<K,V>    implements Map<K,V>, Cloneable, Serializable {}
```

从中我们可以了解到：

- `HashMap<K,V>`：`HashMap`是以`key-value`形式存储数据的。
- `extends AbstractMap<K,V>`：继承了`AbstractMap`，大大减少了实现Map接口时需要的工作量。
- `implements Map<K,V>`：实现了`Map`，提供了所有可选的`Map`操作。
- `implements Cloneable`：表明其可以调用`clone()`方法来返回实例的`field-for-field`拷贝。
- `implements Serializable`：表明该类是可以序列化的。

![1601693853158](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201003105734-514068.png)



## 1.3 put()数据原理分析图解

![1601357215805](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200929132656-232566.png)



## 1.4 一些名词

1. hashmap的底层数据结构名为table的数组，是一个Node数组
2. table数组中的每个元素是一个Node元素（但是这个Node元素可能指向下一个Node元素从而形成链表），table数组的每个位置称为桶，比如talbe[0] 称为一个桶，也可以称为一个bin



# 2. 源码

## 2.1 核心属性分析

### 静态常量

```java
    /**
     * The default initial capacity - MUST be a power of two.
     * 默认的初始容量，必须是二的次方
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     * 
     * 最大容量，当通过构造函数隐式指定了一个大于MAXIMUM_CAPACITY的时候使用
     */
    static final int  MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     * 默认加载因子，当构造函数没有指定加载因子的时候的默认值的时候使用
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     * 
     * TREEIFY_THRESHOLD为当一个bin从list转化为tree的阈值，当一个bin中元素的总元素最低超过这个值的时候，bin才被转化为tree；
     * 为了满足转化为简单bin时的要求，TREEIFY_THRESHOLD必须比2大而且比8要小
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     * 
     * bin反tree化时的最大值，应该比TREEIFY_THRESHOLD要小，
     * 为了在移除元素的时候能检测到移除动作，UNTREEIFY_THRESHOLD必须至少为6
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.
     * 
     * 树化的另外一个阈值，table的长度(注意不是bin的长度)的最小得为64。为了避免扩容和树型结构化阈值之间的冲突，MIN_TREEIFY_CAPACITY 应该最小是 4 * TREEIFY_THRESHOLD
     */
    static final int MIN_TREEIFY_CAPACITY = 64;
   
  
```

### 成员变量

```java
 /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * (We also tolerate length zero in some operations to allow
     * bootstrapping mechanics that are currently not needed.)
     * 
     * table，第一次被使用的时候才进行加载
     */
    transient Node<K,V>[] table;

    /**
     * Holds cached entrySet(). Note that AbstractMap fields are used
     * for keySet() and values().
     * 键值对缓存，它们的映射关系集合保存在entrySet中。即使Key在外部修改导致hashCode变化，缓存中还可以找到映射关系
     */
    transient Set<Map.Entry<K,V>> entrySet;

    /**
     * The number of key-value mappings contained in this map.
     * table中 key-value 元素的个数
     */
    transient int size;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     * 
     * HashMap在结构上被修改的次数，结构上被修改是指那些改变HashMap中映射的数量或者以其他方式修改其内部结构的次数（例如，rehash）。
     * 此字段用于使HashMap集合视图上的迭代器快速失败。
     */
    transient int modCount;

    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * 下一次resize扩容阈值，当前table中的元素超过此值时，触发扩容
     * threshold = capacity * load factor
     * @serial
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.（???????）)
    int threshold;

    /**
     * The load factor for the hash table.
     * 负载因子
     * @serial
     */
    final float loadFactor;

```



## 2.2 构造方法分析

仅仅看最长参数的构造方法就行了，其它三个都是调用了此构造方法：

```java
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * Returns a power of two size for the given target capacity.
     * 
     * 1.返回一个大于等于当前值cap的一个的数字，并且这个数字一定是2的次方数
     * 假如cap为10，那么n= 9 = 0b1001
     * 0b1001 | 0b0100 = 0b1101
     * 0b1101 | 0b0011 = 0b1111
     * 0b1111 | 0b0011 = 0b1111
     * ......
     * .....
     * n = 0b1111 = 15
     * 
     * 2.这里的cap必须要减1，如果不减，并且如果传入的cap为16，那么算出来的值为32
     * 
     * 3.这个方法就是为了把最高位1的后面都变为1
     * 0001 1101 1100 -> 0001 1111 1111 -> +1 -> 0010 1111 1111
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
```

## 2.3 put方法分析

```java
    /**
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or
     *         null if there was no mapping for key.
     *         (A null return can also indicate that the map
     *         previously associated null with key.)
     *         返回先前key对应的value值（如果value为null，也返回null），如果先前不存在这个key，那么返回的就是null；
     */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    /
    * 在往haspmap中插入一个元素的时候，由元素的hashcode经过一个扰动函数之后再与table的长度进行与运算才找到插入位置，下面的这个hash()方法就是所谓的扰动函数
     * 作用：让key的hashCode值的高16位参与运算,hash()方法返回的值的低十六位是有hashCode的高低16位共同的特征的
     * 举例
     * hashCode = 0b 0010 0101 1010 1100  0011 1111 0010 1110
     * 
     *     0b 0010 0101 1010 1100  0011 1111 0010 1110  ^ 
     *     0b 0000 0000 0000 0000  0010 0101 1010 1100 
     *     0b 0010 0101 1010 1100  0001 1010 1000 0010
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```



```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        // tab表示当前hashmap的table
        // p表示table的元素
        // n表示散列表的长度
        // i表示路由寻址结果
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        
        // 延迟初始化逻辑，第一次调用putval()方法的时候才进行初始化hashmap中最耗内存的talbe
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        
        // 1.最简单的一种情况，寻找到的桶位，刚好是null，这个时候直接构建Node节点放进去就行了
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        
         
        else {
            // e，如果key不为null，并且找到了当前要插入的key一致的node元素，就保存在e中
            // k表示一个临时的key
            Node<K,V> e; K k;
            
            // 2.表示该桶位中的第一个元素与你当前插入的node元素的key一致，表示后序要进行替换操作
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            
            // 3.表示当前桶位已经树化了
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            
            // 4.当前捅位是一个链表
            else {
                for (int binCount = 0; ; ++binCount) {
                    // 4.1 迭代到最后一个元素了也没有找到要插入的key一致的node
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }

                    // 4.1 找到了与要插入的key一致的node元素
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            // 如果找到了与要插入的key一致的node元素，那么进行替换
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        // nodeCount表示散列表table结构的修改次数，替换Node元素的value不算
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
```





## 2.4 resize方法分析

当在table长度位16中的元素移到table长度位32的table中的时候；我们可以知道，原来在15这个槽位的元素的hash()值的后四位一定是1111（因为跟1111即table长度-1  进行与运算得到了1111）。所以所以当table长度变为32的时候，原来在15这个槽位的元素要么还在15这个槽位，要么在31这个操作（因为原来15这个槽位的元素后五位一定是11111或者01111，跟 11111即table新长度-1 进行与运算一定得到 01111或者11111）

![1601638246856](https://gitee.com/gu_chun_bo/picture/raw/master/image/20201002193049-143709.png)

```java
/**
 * 对table进行初始化或者扩容。
 * 如果table为null，则对table进行初始化
 * 如果对table扩容，因为每次扩容都是翻倍，与原来计算（n-1）&hash的结果相比，节点要么就在原来的位置，要么就被分配到“原位置+旧容量”这个位置。
 */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        // oldCap表示扩容之前table数组的长度
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        // oldThr表示本次扩容之前的阈值，触发本次扩容操作的阈值
        int oldThr = threshold;
        // newCap：表示扩容之后table数组的大小； newThr表示扩容之后，下次触发扩容的条件
        int newCap, newThr = 0;
        //===================给newCap和newThr赋值start=============================
        // oldCap大于零，说明之前已经初始化过了（hashmap中的散列表不是null），要进行正常的扩容操作
        if (oldCap > 0) {
            // 已经最大值了，不再扩容了
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // （1）进行翻倍扩容(假如旧的oldCap为8， < DEFAULT_INITIAL_CAPACITY，那么此条件不成立newThr将不会赋值)
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        // （2）
        // oldCap == 0（说明hashmap中的散列表是null）且oldThr > 0 ；下面几种情况都会出现oldCap == 0,oldThr > 0
        // 1.public HashMap(int initialCapacity);
        // 2.public HashMap(Map<? extends K, ? extends V> m);并且这个map有数据
        // 3.public HashMap(int initialCapacity, float loadFactor);
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        // oldCap == 0, oldThr == 0
        // public HashMap();
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        // 对应上面（1）不成立或者（2）成立的情况
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }
        //===================给newCap和newThr赋值end=============================
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                // 头结点不为空
                if ((e = oldTab[j]) != null) {
                    // 将对应的桶位指向null，方便jvm回收
                    oldTab[j] = null;

                    // 1.如果只有一个节点
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;

                    // 2.树化了
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);

                    // 3.还是链表
                    else { // preserve order


                        // 低位链表：存放在扩容之后的数组下标的位置，与当前数组下标位置一致的元素
                        // 高位链表：存放在扩容之后的数组下标的位置为当前数组下标位置+ 扩容之前数组长度的元素
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;



                        Node<K,V> next;
                        do {
                            next = e.next;

                            // 比如e.hash只能为两种可能  1 1111 或者 0 1111 ， oldCap 为 10000

                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);

                        // 如果低位链表有数据
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        // 如果高位链表有数据
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
```

## 2.5 get方法分析

```java
   public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

   final Node<K,V> getNode(int hash, Object key) {
        // tab：引用当前hashmap的table
        // first：桶位中的头元素
        // n：table的长度
        // e：是临时Node元素
        // k：是key的临时变量
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
       
       // 1.如果哈希表为空，或key对应的桶为空，返回null
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            
            // 2.这个桶的头元素就是想要找的
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            
            // 说明当前桶位不止一个元素，可能是链表，也可能是红黑树
            if ((e = first.next) != null) {
                // 3.树化了
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                
                // 4.链表
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
```

## 2.6 remove方法分析

```java
    public V remove(Object key) {
        Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }    

    final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
        // tab：引用当前hashmap的table
        // p：当前的node元素
        // n：当前的散列表数组长度
        // index：表示寻址结果
        Node<K,V>[] tab; Node<K,V> p; int n, index;

        // 1.如果数组table为空或key映射到的桶为空，返回null。
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {

            // node：查找到的结果
            // e：当前Node的下一个元素
            Node<K,V> node = null, e; K k; V v;

            // 2.桶位的头元素就是我们要找的
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;

            else if ((e = p.next) != null) {
                // 3.树化了
                if (p instanceof TreeNode)
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                // 4.链表中
                else {
                    do {
                        if (e.hash == hash &&
                                ((k = e.key) == key ||
                                        (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }

            // 如果node不为null，说明按照key查找到想要删除的数据了
            if (node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                // 是树，删除节点
                if (node instanceof TreeNode)
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
                // 删除的桶的第一个元素
                else if (node == p)
                    tab[index] = node.next;
                // 不是第一个元素
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }

```





## 2.7 replace方法分析



```java
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Node<K,V> e; V v;
        if ((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        Node<K,V> e;
        if ((e = getNode(hash(key), key)) != null) {
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }
```



## 2.8 其它常用方法

### isEmpty()

```java
/**
 * 如果map中没有键值对映射，返回true
 * 
 * @return <如果map中没有键值对映射，返回true
 */
public boolean isEmpty() {
    return size == 0;
}
```

### putMapEntries()

```java
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            // table为null，代表这里使用HashMap(Map<? extends K, ? extends V> m)构造函数 或者其它方式实例化hashmap但是还没往里面添加过元素
            if (table == null) { // pre-size
                //前面讲到，initial capacity*load factor就是当前hashMap允许的最大元素数目。那么不难理解，s/loadFactor+1即为应该初始化的容量。
                float ft = ((float)s / loadFactor) + 1.0F;
                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                        (int)ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }
            //table已经初始化，并且map的大小大于临界值
            else if (s > threshold)
                //扩容处理
                resize();
            //将map中所有键值对添加到hashMap中
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }
```

### putAll()

```java
/**
 * 将参数map中的所有键值对映射插入到hashMap中，如果有碰撞，则覆盖value。
 * @param m 参数map
 * @throws NullPointerException 如果map为null
 */
public void putAll(Map<? extends K, ? extends V> m) {
    putMapEntries(m, true);
}
```

### clear()

```java
/**
 * 删除map中所有的键值对
 */
public void clear() {
    Node<K,V>[] tab;
    modCount++;
    if ((tab = table) != null && size > 0) {
        size = 0;
        for (int i = 0; i < tab.length; ++i)
            tab[i] = null;
    }
}
```

### containsValue( Object value)

```java
/**
 * 如果hashMap中的键值对有一对或多对的value为参数value，返回true
 *
 * @param value 参数value
 * @return 如果hashMap中的键值对有一对或多对的value为参数value，返回true
 */
public boolean containsValue(Object value) {
    Node<K,V>[] tab; V v;
    //
    if ((tab = table) != null && size > 0) {
        //遍历数组table
        for (int i = 0; i < tab.length; ++i) {
            //遍历桶中的node
            for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                if ((v = e.value) == value ||
                    (value != null && value.equals(v)))
                    return true;
            }
        }
    }
    return false;
}
```

# 3.参考

1. [视频](https://www.bilibili.com/video/BV1LJ411W7dP)
2. [3y](https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247484139&idx=1&sn=bb73ac07081edabeaa199d973c3cc2b0&chksm=ebd743eadca0cafc532f298b6ab98b08205e87e37af6a6a2d33f5f2acaae245057fa01bd93f4&scene=21#wechat_redirect)
3. [csdn](https://blog.csdn.net/panweiwei1994/article/details/77244920)