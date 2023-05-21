# SandboxTown

沙盒小镇，基于 Vue & Phaser.js & SpringBoot & WebSocket & MySQL & Redis 的沙盒游戏

## 构建方法



## 功能介绍



## 待实现功能

### 建筑系统

- 建筑包含商店、农田、栅栏等
- 玩家可以采集材料、建造建筑和装饰建筑
- 玩家可以自由设计建筑内部家具和物品摆放
- 建筑可以升级，提高产出和效率

### 经济系统

- 玩家可以通过经营商店、种植农作物、养殖动物等方式赚取金钱和经验值
- 金钱可以购买更多的材料和装饰品，扩展自己的小镇规模
- 经验值可以提高农作物收成等
- 玩家可以与其他玩家交易物品

### 冒险任务

- 玩家可以完成任务获得奖励，比如金钱、材料、经验值等等
- 任务可以包括打败敌人、收集稀有物品等等
- 任务应该有不同难度级别和奖励

### 多人游戏

- 玩家可以与其他玩家一起合作或竞争
- 同一地图上应该能够同时容纳多个玩家

### AI虚拟人物

- 游戏中可以加入基于ChatGPT的AI虚拟人物
- 这些虚拟人物可以与玩家进行对话，提供游戏中的帮助和提示
- 虚拟人物也可以帮助玩家处理一些任务
- 玩家也可以饲养宠物





玩家需要注册账号（提供邮箱、用户名和密码）和登录账号才能进行游戏

可以选择地图

建筑包含商店、树、草地、池塘、工厂、铁矿等，商店可以进行买卖交易，但是卖出价格很低，这也为玩家自己创建商店卖东西提供了价格优势，池塘可以捕鱼（需要手持鱼竿），铁矿可以挖铁（需要手持锄头）、树可以用来砍（手持锯子时）或摘苹果（手不持锯子时），工厂可以使用各种物品合成各种商店买不到的东西，例如各种建筑。这些建筑里面可获得的东西，量都是有限的，当然也会定期自动补充。另外，捕鱼、挖矿、砍树、摘苹果需要花费一定时间的。

玩家可以创建各种建筑，玩家创建的商店，可以自己可以放置商品（价格也是自己决定），玩家创建的池塘，如果被别人捕鱼，自己也会获得部分收益

玩家之间可以聊天，玩家也可以广播消息

时间包含黎明、白天、黄昏、晚上，只有晚上有怪，黎明时怪开始受伤死亡。只有晚上玩家、怪、宠物之间才能战斗。界面上要显示当前时间段对应的图标

玩家可以饲养宠物，狗会跟随你，在自己或主人遭到攻击时会反击，猫会不定期为你捕鱼，并且跑得很快，别人较难攻击到，玩家养的牛可以宰杀，收获牛革和牛肉，牛革可以做皮质胸甲以及其他物品，牛会在草地附近移动，在草地附近才会繁殖，牛有寿命，和猫狗不一样，牛也不能被怪物或其他玩家攻击，这点也和猫狗不一样

界面背景有一些交叉的小路，显得美观

玩家有金钱、经验值、等级、饱腹值、血量、攻击、防御、速度这些属性，都是顾名思义。经验值每到100就归0，等级升一级。饱腹值最高为100，高于80时会定期回复血量，

玩家可以上装备，有护甲、鞋子、左手、右手四种，装备不仅可以更新玩家的属性值，还可以有一些神奇的效果，例如剧毒手套可以使攻击对方时对方持续性的伤血。这样也就引出了玩家的状态，例如中毒状态

玩家可以食用食物，以提高各种属性值，也能使玩家进入某种状态，例如虚无状态（当晚受到攻击有30%概率miss）

玩家的等级提升会提高各属性值，并解锁之前不能购买的商品或者是工厂里不能制作的东西

怪物分为近身攻击和远程攻击两种，它们会寻找一定范围内的玩家，并进行攻击。近身攻击的怪是蜘蛛，蜘蛛在遭到碰撞时会旋转

点击左键显示一个东西的信息，点击右键攻击他，在地图上右键一个位置移动到那里

点击自己进行范围攻击

移动端适配：点击一个东西攻击他，在地图上点击一个位置移动到那里，长按显示一个东西的信息

## 设计文档

参见[设计文档](doc/设计文档.md)

