# Java_design
### UML类图关系
| name | relationship | example |
|:------:|:--------------|---------|
|泛化 Generalization | 继承关系 | `————————▷` |
|实现 Realization | 类与接口 | `---------▷` |
|关联 Association | 双向 & 单向 | `老师————学生` & `学生————>课程` |
|聚合 Aggregation | 整体与部分(可独立) | `汽车◇————————>轮胎` |
|组合 Composition | 整体与部分(不可独立) | `公司◆————————>部门` |
|依赖 Dependency | 使用关系 | `-------->` |

**关系强弱**
泛化=实现\>组合\>聚合\>关联\>依赖

### 设计模式六大原则
 - 开闭原则(Open Close Principle)：提高扩展性
 - 里氏代换原则(Liskov Substitution Principle)：子父类互相替换
 - 依赖倒转原则(Dependence Inversion Principle)：针对接口编程，依赖抽象不依赖具体
 - 接口隔离原则(Interface Segregation Principle)：降低耦合度
 - 迪米特法则 or 最少知道原则(Demeter Principle)：功能模块尽量独立
 - 合成复用原则(Composite Reuse Principle)：尽量使用聚合，组合，而不是继承
 
### 设计模式类型
 - 创建型模式-->对象怎么来
 - 结构型模式-->对象和谁有关
 - 行为型模式-->对象与对象在干嘛
 - J2EE 模式-->对象合起来要干嘛