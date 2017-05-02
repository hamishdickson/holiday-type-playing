package playing

object Playing {

  sealed trait Limits
  final case class StringLimit(s: String) extends Limits
  final case class IntLimit(i: Int) extends Limits

  trait A[T <: Foo[_]] {
    type O <: Limits
    def f(t: T): O
  }

  // note, these have to be declared *before* the ADT otherwise it won't compile
  implicit val barA = new A[Bar] {
    type O = StringLimit
    def f(t: Bar): O = StringLimit(t.s)
  }

  // note, if you don't have this, then you get a compilation error :)
  /*
  [info] Compiling 1 Scala source to /Users/hamishdickson/Programming/scala-stuff/holiday-type-playing/target/scala-2.12/classes...
[error] /Users/hamishdickson/Programming/scala-stuff/holiday-type-playing/Playing.scala:22: could not find implicit value for parameter evidence: playing.A[playing.Playing.Baz]
[error]   final case class Baz(i: Int) extends Foo[Baz]
[error]                                        ^
[error] one error found
[error] (compile:compileIncremental) Compilation failed
[error] Total time: 0 s, completed 21-Apr-2017 17:08:46
*/
  implicit val bazA = new A[Baz] {
    type O = IntLimit
    def f(t: Baz): O = IntLimit(t.i)
  }

  // f-bounded polymorphism
  // note, a trait won't compile here
  sealed abstract class Foo[B <: Foo[B]](implicit val evidence: A[B]) {
    def fu(b: B) = implicitly[A[B]].f(b)
  }
  final case class Bar(s: String) extends Foo[Bar]
  final case class Baz(i: Int) extends Foo[Baz]

  object Foo

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

  val bars = List(Bar("a"), Bar("b"), Bar("c"))
  val bazs = List(Baz(1), Baz(2), Baz(3))
  val foos = List(Bar("a"), Baz(1), Bar("b"), Baz(2))

  def g(xs: List[Foo[_]]): List[Limits] = xs.map(x => ???)

  val x = g(bars)
  val y = g(bazs)
  val z = g(foos)

}
