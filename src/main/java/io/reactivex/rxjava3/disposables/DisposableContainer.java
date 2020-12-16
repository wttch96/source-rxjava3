package io.reactivex.rxjava3.disposables;

/**
 * 通用接口, 用于从容器中添加和移除一次性物品.
 *
 * @since 2.0
 */
public interface DisposableContainer {

  /**
   * 将一次性用品添加到此容器中, 或将其丢弃(如果已将其丢弃).
   *
   * @param d 添加一次性操作, 不为null
   * @return 如果成功, 则返回true; 如果已丢弃此容器, 则返回false
   */
  boolean add(Disposable d);

  /**
   * 如果给定的一次性用品是该容器的一部分, 则将其除去并处置.
   *
   * @param d 一次性物品以移除和处置, 不为null
   * @return 如果操作成功, 则为true
   */
  boolean remove(Disposable d);

  /**
   * 如果给定的一次性用品是该容器的一部分，则将其除去但不进行处理.
   *
   * @param d 一次性移除, 不为null
   * @return 如果操作成功, 则为true
   */
  boolean delete(Disposable d);
}
