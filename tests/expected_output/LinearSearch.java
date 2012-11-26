LinearSearch.main:
       t0 := new LS
       param t0
       param 10
       t1 := call LS.Start, 2
       param t1
       call System.out.println, 1

LS.Start:
       param this
       param sz
       t0 := call LS.Init, 2
       aux01 := t0
       param this
       t1 := call LS.Print, 1
       aux02 := t1
       param 9999
       call System.out.println, 1
       param this
       param 8
       t2 := call LS.Search, 2
       param t2
       call System.out.println, 1
       param this
       param 12
       t3 := call LS.Search, 2
       param t3
       call System.out.println, 1
       param this
       param 17
       t4 := call LS.Search, 2
       param t4
       call System.out.println, 1
       param this
       param 50
       t5 := call LS.Search, 2
       param t5
       call System.out.println, 1
       return 55

LS.Print:
       j := 1
L0:    t0 := j < size
       iftrue t0 goto L1
       goto L2
L1:    t1 := number[j]
       param t1
       call System.out.println, 1
       t2 := j + 1
       j := t2
       goto L0
L2:    return 0

LS.Search:
       j := 1
       ls01 := false
       ifound := 0
L3:    t0 := j < size
       iftrue t0 goto L4
       goto L5
L4:    t1 := number[j]
       aux01 := t1
       t2 := num + 1
       aux02 := t2
       t3 := aux01 < num
       iftrue t3 goto L6
       t4 := aux01 < aux02
       t5 := ! t4
       iftrue t5 goto L7
       ls01 := true
       ifound := 1
       j := size
       goto L8
L7:    nt := 0
L8:    goto L9
L6:    nt := 0
L9:    t6 := j + 1
       j := t6
       goto L3
L5:    return ifound

LS.Init:
       size := sz
       t0 := new int[], sz
       number := t0
       j := 1
       t1 := size + 1
       k := t1
L10:   t2 := j < size
       iftrue t2 goto L11
       goto L12
L11:   t3 := 2 * j
       aux01 := t3
       t4 := k - 3
       aux02 := t4
       t5 := aux01 + aux02
       number[j] := t5
       t6 := j + 1
       j := t6
       t7 := k - 1
       k := t7
       goto L10
L12:   return 0