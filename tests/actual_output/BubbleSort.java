BubbleSort.main:
       t0 := new BBS
       param t0
       param 10
       t1 := call BBS.Start, 2
       param t1
       call System.out.println, 1

BBS.Start:
       param this
       param sz
       t0 := call BBS.Init, 2
       aux01 := t0
       param this
       t1 := call BBS.Print, 1
       aux01 := t1
       param 99999
       call System.out.println, 1
       param this
       t2 := call BBS.Sort, 1
       aux01 := t2
       param this
       t3 := call BBS.Print, 1
       aux01 := t3
       return 0

BBS.Sort:
       t0 := size - 1
       i := t0
       t1 := 0 - 1
       aux02 := t1
L0:    t2 := aux02 < i
       iftrue t2 goto L1
       goto L2
L1:    j := 1
L3:    t3 := i + 1
       t4 := j < t3
       iftrue t4 goto L4
       goto L5
L4:    t5 := j - 1
       aux07 := t5
       t6 := number[aux07]
       aux04 := t6
       t7 := number[j]
       aux05 := t7
       t8 := aux05 < aux04
       iftrue t8 goto L6
       nt := 0
       goto L7
L6:    t9 := j - 1
       aux06 := t9
       t10 := number[aux06]
       t := t10
       t11 := number[j]
       number[aux06] := t11
       number[j] := t
L7:    t12 := j + 1
       j := t12
       goto L3
L5:    t13 := i - 1
       i := t13
       goto L0
L2:    return 0

BBS.Print:
       j := 0
L8:    t0 := j < size
       iftrue t0 goto L9
       goto L10
L9:    t1 := number[j]
       param t1
       call System.out.println, 1
       t2 := j + 1
       j := t2
       goto L8
L10:   return 0

BBS.Init:
       size := sz
       t0 := new int[], sz
       number := t0
       number[0] := 20
       number[1] := 7
       number[2] := 12
       number[3] := 18
       number[4] := 2
       number[5] := 11
       number[6] := 6
       number[7] := 9
       number[8] := 19
       number[9] := 5
       return 0

