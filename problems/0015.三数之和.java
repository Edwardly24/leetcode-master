<p align="center">
<a href="https://programmercarl.com/other/kstar.html" target="_blank">
  <img src="https://code-thinking-1253855093.file.myqcloud.com/pics/20210924105952.png" width="1000"/>
</a>
<p align="center"><strong><a href="https://mp.weixin.qq.com/s/tqCxrMEU-ajQumL1i8im9A">参与本项目</a>，贡献其他语言版本的代码，拥抱开源，让更多学习算法的小伙伴们收益！</strong></p>



> 用哈希表解决了[两数之和](https://programmercarl.com/0001.两数之和.html)，那么三数之和呢？

# 第15题. 三数之和

[力扣题目链接](https://leetcode-cn.com/problems/3sum/)

给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。

**注意：** 答案中不可以包含重复的三元组。

示例：

给定数组 nums = [-1, 0, 1, 2, -1, -4]，

满足要求的三元组集合为：
[
  [-1, 0, 1],
  [-1, -1, 2]
]


# 思路

**注意[0， 0， 0， 0] 这组数据**

## 哈希解法

两层for循环就可以确定 a 和b 的数值了，可以使用哈希法来确定 0-(a+b) 是否在 数组里出现过，其实这个思路是正确的，但是我们有一个非常棘手的问题，就是题目中说的不可以包含重复的三元组。

把符合条件的三元组放进vector中，然后再去重，这样是非常费时的，很容易超时，也是这道题目通过率如此之低的根源所在。

去重的过程不好处理，有很多小细节，如果在面试中很难想到位。

时间复杂度可以做到$O(n^2)$，但还是比较费时的，因为不好做剪枝操作。

大家可以尝试使用哈希法写一写，就知道其困难的程度了。

哈希法C++代码:
```CPP
class Solution {
public:
    vector<vector<int>> threeSum(vector<int>& nums) {
        vector<vector<int>> result;
        sort(nums.begin(), nums.end());
        // 找出a + b + c = 0
        // a = nums[i], b = nums[j], c = -(a + b)
        for (int i = 0; i < nums.size(); i++) {
            // 排序之后如果第一个元素已经大于零，那么不可能凑成三元组
            if (nums[i] > 0) {
                break;
            }
            if (i > 0 && nums[i] == nums[i - 1]) { //三元组元素a去重
                continue;
            }
            unordered_set<int> set;
            for (int j = i + 1; j < nums.size(); j++) {
                if (j > i + 2
                        && nums[j] == nums[j-1]
                        && nums[j-1] == nums[j-2]) { // 三元组元素b去重
                    continue;
                }
                int c = 0 - (nums[i] + nums[j]);
                if (set.find(c) != set.end()) {
                    result.push_back({nums[i], nums[j], c});
                    set.erase(c);// 三元组元素c去重
                } else {
                    set.insert(nums[j]);
                }
            }
        }
        return result;
    }
};
```

## 双指针

**其实这道题目使用哈希法并不十分合适**，因为在去重的操作中有很多细节需要注意，在面试中很难直接写出没有bug的代码。

而且使用哈希法 在使用两层for循环的时候，能做的剪枝操作很有限，虽然时间复杂度是$O(n^2)$，也是可以在leetcode上通过，但是程序的执行时间依然比较长 。

接下来我来介绍另一个解法：双指针法，**这道题目使用双指针法 要比哈希法高效一些**，那么来讲解一下具体实现的思路。

动画效果如下：

![15.三数之和](https://code-thinking.cdn.bcebos.com/gifs/15.%E4%B8%89%E6%95%B0%E4%B9%8B%E5%92%8C.gif)

拿这个nums数组来举例，首先将数组排序，然后有一层for循环，i从下标0的地方开始，同时定一个下标left 定义在i+1的位置上，定义下标right 在数组结尾的位置上。

依然还是在数组中找到 abc 使得a + b +c =0，我们这里相当于  a = nums[i] b = nums[left]  c = nums[right]。

接下来如何移动left 和right呢， 如果nums[i] + nums[left] + nums[right] > 0  就说明 此时三数之和大了，因为数组是排序后了，所以right下标就应该向左移动，这样才能让三数之和小一些。

如果 nums[i] + nums[left] + nums[right] < 0 说明 此时 三数之和小了，left 就向右移动，才能让三数之和大一些，直到left与right相遇为止。

时间复杂度：$O(n^2)$。

C++代码代码如下：

```CPP
class Solution {
public:
    vector<vector<int>> threeSum(vector<int>& nums) {
        vector<vector<int>> result;
        sort(nums.begin(), nums.end());
        // 找出a + b + c = 0
        // a = nums[i], b = nums[left], c = nums[right]
        for (int i = 0; i < nums.size(); i++) {
            // 排序之后如果第一个元素已经大于零，那么无论如何组合都不可能凑成三元组，直接返回结果就可以了
            if (nums[i] > 0) {
                return result;
            }
            // 错误去重方法，将会漏掉-1,-1,2 这种情况
            /*
            if (nums[i] == nums[i + 1]) {
                continue;
            }
            */
            // 正确去重方法
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.size() - 1;
            while (right > left) {
                // 去重复逻辑如果放在这里，0，0，0 的情况，可能直接导致 right<=left 了，从而漏掉了 0,0,0 这种三元组
                /*
                while (right > left && nums[right] == nums[right - 1]) right--;
                while (right > left && nums[left] == nums[left + 1]) left++;
                */
                if (nums[i] + nums[left] + nums[right] > 0) {
                    right--;
                } else if (nums[i] + nums[left] + nums[right] < 0) {
                    left++;
                } else {
                    result.push_back(vector<int>{nums[i], nums[left], nums[right]});
                    // 去重逻辑应该放在找到一个三元组之后
                    while (right > left && nums[right] == nums[right - 1]) right--;
                    while (right > left && nums[left] == nums[left + 1]) left++;

                    // 找到答案时，双指针同时收缩
                    right--;
                    left++;
                }
            }

        }
        return result;
    }
};
```

# 思考题


既然三数之和可以使用双指针法，我们之前讲过的[1.两数之和](https://programmercarl.com/0001.两数之和.html)，可不可以使用双指针法呢？

如果不能，题意如何更改就可以使用双指针法呢？  **大家留言说出自己的想法吧！**

两数之和 就不能使用双指针法，因为[1.两数之和](https://programmercarl.com/0001.两数之和.html)要求返回的是索引下标， 而双指针法一定要排序，一旦排序之后原数组的索引就被改变了。

如果[1.两数之和](https://programmercarl.com/0001.两数之和.html)要求返回的是数值的话，就可以使用双指针法了。




## 其他语言版本


Java：
```Java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                return result;
            }

            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int left = i + 1;
            int right = nums.length - 1;
            while (right > left) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum > 0) {
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    while (right > left && nums[right] == nums[right - 1]) right--;
                    while (right > left && nums[left] == nums[left + 1]) left++;
                    
                    right--; 
                    left++;
                }
            }
        }
        return result;
    }
}
```

Python：
```Python
class Solution:
    def threeSum(self, nums):
        ans = []
        n = len(nums)
        nums.sort()
        for i in range(n):
            left = i + 1
            right = n - 1
            if nums[i] > 0:
                break
            if i >= 1 and nums[i] == nums[i - 1]:
                continue
            while left < right:
                total = nums[i] + nums[left] + nums[right]
                if total > 0:
                    right -= 1
                elif total < 0:
                    left += 1
                else:
                    ans.append([nums[i], nums[left], nums[right]])
                    while left != right and nums[left] == nums[left + 1]: left += 1
                    while left != right and nums[right] == nums[right - 1]: right -= 1
                    left += 1
                    right -= 1
        return ans
```
Go：
```Go
func threeSum(nums []int)[][]int{
	sort.Ints(nums)
	res:=[][]int{}
	
	for i:=0;i<len(nums)-2;i++{
		n1:=nums[i]
		if n1>0{
			break
		}
		if i>0&&n1==nums[i-1]{
			continue
		}
		l,r:=i+1,len(nums)-1
		for l<r{
			n2,n3:=nums[l],nums[r]
			if n1+n2+n3==0{
				res=append(res,[]int{n1,n2,n3})
				for l<r&&nums[l]==n2{
					l++
				}
				for l<r&&nums[r]==n3{
					r--
				}
			}else if n1+n2+n3<0{
				l++
			}else {
				r--
			}
		}
	}
	return res
}
```

javaScript: 

```js
/**
 * @param {number[]} nums
 * @return {number[][]}
 */

// 循环内不考虑去重 
var threeSum = function(nums) {
    const len = nums.length;
    if(len < 3) return [];
    nums.sort((a, b) => a - b);
    const resSet = new Set();
    for(let i = 0; i < len - 2; i++) {
        if(nums[i] > 0) break;
        let l = i + 1, r = len - 1;
        while(l < r) {
            const sum = nums[i] + nums[l] + nums[r];
            if(sum < 0) { l++; continue };
            if(sum > 0) { r--; continue };
            resSet.add(`${nums[i]},${nums[l]},${nums[r]}`);
            l++;
            r--;
        }
    }
    return Array.from(resSet).map(i => i.split(","));
};

// 去重优化
var threeSum = function(nums) {
    const len = nums.length;
    if(len < 3) return [];
    nums.sort((a, b) => a - b);
    const res = [];
    for(let i = 0; i < len - 2; i++) {
        if(nums[i] > 0) break;
        // a去重
        if(i > 0 && nums[i] === nums[i - 1]) continue;
        let l = i + 1, r = len - 1;
        while(l < r) {
            const sum = nums[i] + nums[l] + nums[r];
            if(sum < 0) { l++; continue };
            if(sum > 0) { r--; continue };
            res.push([nums[i], nums[l], nums[r]])
            // b c 去重 
            while(l < r && nums[l] === nums[++l]);
            while(l < r && nums[r] === nums[--r]);
        }
    }
    return res;
};
```
TypeScript:

```typescript
function threeSum(nums: number[]): number[][] {
    nums.sort((a, b) => a - b);
    let length = nums.length;
    let left: number = 0,
        right: number = length - 1;
    let resArr: number[][] = [];
    for (let i = 0; i < length; i++) {
        if (i > 0 && nums[i] === nums[i - 1]) {
            continue;
        }
        left = i + 1;
        right = length - 1;
        while (left < right) {
            let total: number = nums[i] + nums[left] + nums[right];
            if (total === 0) {
                resArr.push([nums[i], nums[left], nums[right]]);
                left++;
                right--;
                while (nums[right] === nums[right + 1]) {
                    right--;
                }
                while (nums[left] === nums[left - 1]) {
                    left++;
                }
            } else if (total < 0) {
                left++;
            } else {
                right--;
            }
        }
    }
    return resArr;
};
```

ruby:
```ruby
def is_valid(strs)
    symbol_map = {')' => '(', '}' => '{', ']' => '['}
    stack = []
    strs.size.times {|i|
        c = strs[i]
        if symbol_map.has_key?(c)
            top_e = stack.shift
            return false if symbol_map[c] != top_e
        else
            stack.unshift(c)
        end
    }
    stack.empty?
end
```


PHP:
```php
class Solution {
    /**
     * @param Integer[] $nums
     * @return Integer[][]
     */
    function threeSum($nums) {
        $res = [];
        sort($nums);
        for ($i = 0; $i < count($nums); $i++) {
            if ($nums[$i] > 0) {
                return $res;
            }
            if ($i > 0 && $nums[$i] == $nums[$i - 1]) {
                continue;
            }
            $left = $i + 1;
            $right = count($nums) - 1;
            while ($left < $right) {
                $sum = $nums[$i] + $nums[$left] + $nums[$right];
                if ($sum < 0) {
                    $left++;
                }
                else if ($sum > 0) {
                    $right--;
                }
                else {
                    $res[] = [$nums[$i], $nums[$left], $nums[$right]];
                    while ($left < $right && $nums[$left] == $nums[$left + 1]) $left++;
                    while ($left < $right && $nums[$right] == $nums[$right - 1]) $right--;
                    $left++;
                    $right--;
                }
            }
        }
        return $res;
    }
}
```

Swift:
```swift
// 双指针法
func threeSum(_ nums: [Int]) -> [[Int]] {
    var res = [[Int]]()
    var sorted = nums
    sorted.sort()
    for i in 0 ..< sorted.count {
        if sorted[i] > 0 {
            return res
        }
        if i > 0 && sorted[i] == sorted[i - 1] {
            continue
        }
        var left = i + 1
        var right = sorted.count - 1
        while left < right {
            let sum = sorted[i] + sorted[left] + sorted[right]
            if sum < 0 {
                left += 1
            } else if sum > 0 {
                right -= 1
            } else {
                res.append([sorted[i], sorted[left], sorted[right]])
                
                while left < right && sorted[left] == sorted[left + 1] {
                    left += 1
                }
                while left < right && sorted[right] == sorted[right - 1] {
                    right -= 1
                }
                
                left += 1
                right -= 1
            }
        }
    }
    return res
}
```

C:
```C
//qsort辅助cmp函数
int cmp(const void* ptr1, const void* ptr2) {
    return *((int*)ptr1) > *((int*)ptr2);
}

int** threeSum(int* nums, int numsSize, int* returnSize, int** returnColumnSizes) {
    //开辟ans数组空间
    int **ans = (int**)malloc(sizeof(int*) * 18000);
    int ansTop = 0;
    //若传入nums数组大小小于3，则需要返回数组大小为0
    if(numsSize < 3) {
        *returnSize = 0;
        return ans;
    }
    //对nums数组进行排序
    qsort(nums, numsSize, sizeof(int), cmp);
    

    int i;
    //用for循环遍历数组，结束条件为i < numsSize - 2(因为要预留左右指针的位置)
    for(i = 0; i < numsSize - 2; i++) {
        //若当前i指向元素>0，则代表left和right以及i的和大于0。直接break
        if(nums[i] > 0)
            break;
        //去重：i > 0 && nums[i] == nums[i-1]
        if(i > 0 && nums[i] == nums[i-1])
            continue;
        //定义左指针和右指针
        int left = i + 1;
        int right = numsSize - 1;
        //当右指针比左指针大时进行循环
        while(right > left) {
            //求出三数之和
            int sum = nums[right] + nums[left] + nums[i];
            //若和小于0，则左指针+1（因为左指针右边的数比当前所指元素大）
            if(sum < 0)
                left++;
            //若和大于0，则将右指针-1
            else if(sum > 0)
                right--;
            //若和等于0
            else {
                //开辟一个大小为3的数组空间，存入nums[i], nums[left]和nums[right]
                int* arr = (int*)malloc(sizeof(int) * 3);
                arr[0] = nums[i];
                arr[1] = nums[left];
                arr[2] = nums[right];
                //将开辟数组存入ans中
                ans[ansTop++] = arr;
                //去重
                while(right > left && nums[right] == nums[right - 1])
                    right--;
                while(left < right && nums[left] == nums[left + 1])
                    left++;
                //更新左右指针
                left++;
                right--;
            }
        }
    }

    //设定返回的数组大小
    *returnSize = ansTop;
    *returnColumnSizes = (int*)malloc(sizeof(int) * ansTop);
    int z;
    for(z = 0; z < ansTop; z++) {
        (*returnColumnSizes)[z] = 3;
    }
    return ans;
}
```

C#:
```csharp
public class Solution
{
    public IList<IList<int>> ThreeSum(int[] nums)
    {
        var result = new List<IList<int>>();

        Array.Sort(nums);

        for (int i = 0; i < nums.Length - 2; i++)
        {
            int n1 = nums[i];

            if (n1 > 0)
                break;

            if (i > 0 && n1 == nums[i - 1])
                continue;

            int left = i + 1;
            int right = nums.Length - 1;

            while (left < right)
            {
                int n2 = nums[left];
                int n3 = nums[right];
                int sum = n1 + n2 + n3;

                if (sum > 0)
                {
                    right--;
                }
                else if (sum < 0)
                {
                    left++;
                }
                else
                {
                    result.Add(new List<int> { n1, n2, n3 });

                    while (left < right && nums[left] == n2)
                    {
                        left++;
                    }

                    while (left < right && nums[right] == n3)
                    {
                        right--;
                    }
                }
            }
        }

        return result;
    }
}
```

-----------------------
<div align="center"><img src=https://code-thinking.cdn.bcebos.com/pics/01二维码一.jpg width=500> </img></div>
