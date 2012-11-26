Inheritance.main:
	t0 := new Foo
	param t0
	t1 := call Foo.Start, 1
	param t1
	call System.out.println, 1

Foo.Start:
	param this
	t0 := call Foo.A, 1
	a := t0
	param this
	t1 := call Bar.B, 1
	b := t1
	return 0

Foo.A:
	return 1

Bar.B:
	return 1
