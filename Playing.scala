package playing

trait A[T] {
  type O
  def f(t: T): O
}

object Playing {
  implicit val barA = new A[Bar] {
    type O = String
    def f(t: Bar): O = t.s
  }

  implicit val bazA = new A[Baz] {
    type O = Int
    def f(t: Baz): O = t.i
  }

  // f-bounded polymorphism
  sealed abstract class Foo[B <: Foo[B]](implicit val evidence: A[B])
  final case class Bar(s: String) extends Foo[Bar]
  final case class Baz(i: Int) extends Foo[Baz]


  /*
  this no compile (which is what I want - it's not a subtype of Foo)
  > compile
[info] Compiling 1 Scala source to /Users/hamishdickson/Programming/scala-stuff/holiday-type-playing/target/scala-2.12/classes...
[error] /Users/hamishdickson/Programming/scala-stuff/holiday-type-playing/Playing.scala:25: type arguments [playing.Playing.Woozle] do not conform to trait A's type parameter bounds [T <: playing.Playing.Foo]
[error]   implicit val woozleA = new A[Woozle] {
[error]                ^
[error] /Users/hamishdickson/Programming/scala-stuff/holiday-type-playing/Playing.scala:25: type arguments [playing.Playing.Woozle] do not conform to trait A's type parameter bounds [T <: playing.Playing.Foo]
[error]   implicit val woozleA = new A[Woozle] {
[error]                              ^
[error] two errors found
[error] (compile:compileIncremental) Compilation failed
[error] Total time: 0 s, completed 20-Apr-2017 12:47:06

  case class Woozle(d: Double)
  implicit val woozleA = new A[Woozle] {
    type O = Double
    def f(t: Woozle): O = t.d
  }
  */
}
