Factorial.main:
       t0 := new Fac
       param t0
       param 10
       t1 := call Fac.ComputeFac, 2
       param t1
       call System.out.println, 1

Fac.ComputeFac:
       t0 := num < 1
       iftrue t0 goto L0
       param this
       t1 := num - 1
       param t1
       t2 := call Fac.ComputeFac, 2
       t3 := num * t2
       num_aux := t3
       goto L1
L0:    num_aux := 1
L1:    return num_aux