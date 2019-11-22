/**
 * @description 实现了一个简单的Netty的Demo，通过以pipeline的形式增加多个Handler，实现了不同事件的处理；
 * 引入进程组的概念（master-slave模式），能够更好的利用多核CPU，进一步提升了性能；编程也更结构化、简洁。
 * @author lyqlbst
 * @email 1098387108@qq.com
 * @date 2019/11/22 6:39 PM
 */
package priv.nio.demo.netty;