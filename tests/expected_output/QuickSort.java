QuickSort.main:
       t0 := new QS
       param t0
       param 10
       t1 := call QS.Start, 2
       param t1
       call System.out.println, 1

QS.Start:
       param this
       param sz
       t0 := call QS.Init, 2
       aux01 := t0
       param this
       t1 := call QS.Print, 1
       aux01 := t1
       param 9999
       call System.out.println, 1
       t2 := size - 1
       aux01 := t2
       param this
       param 0
       param aux01
       t3 := call QS.Sort, 3
       aux01 := t3
       param this
       t4 := call QS.Print, 1
       aux01 := t4
       return 0

QS.Sort:
       t := 0
       t0 := left < right
       iftrue t0 goto L0
       nt := 0
       goto L1
L0:    t1 := number[right]
       v := t1
       t2 := left - 1
       i := t2
       j := right
       cont01 := true
L2:    iftrue cont01 goto L3
       goto L4
L3:    cont02 := true
L5:    iftrue cont02 goto L6
       goto L7
L6:    t3 := i + 1
       i := t3
       t4 := number[i]
       aux03 := t4
       t5 := aux03 < v
       t6 := ! t5
       iftrue t6 goto L8
       cont02 := true
       goto L9
L8:    cont02 := false
L9:    goto L5
L7:    cont02 := true
L10:   iftrue cont02 goto L11
       goto L12
L11:   t7 := j - 1
       j := t7
       t8 := number[j]
       aux03 := t8
       t9 := v < aux03
       t10 := ! t9
       iftrue t10 goto L13
       cont02 := true
       goto L14
L13:   cont02 := false
L14:   goto L10
L12:   t11 := number[i]
       t := t11
       t12 := number[j]
       number[i] := t12
       number[j] := t
       t13 := i + 1
       t14 := j < t13
       iftrue t14 goto L15
       cont01 := true
       goto L16
L15:   cont01 := false
L16:   goto L2
L4:    t15 := number[i]
       number[j] := t15
       t16 := number[right]
       number[i] := t16
       number[right] := t
       param this
       param left
       t17 := i - 1
       param t17
       t18 := call QS.Sort, 3
       nt := t18
       param this
       t19 := i + 1
       param t19
       param right
       t20 := call QS.Sort, 3
       nt := t20
L1:    return 0

QS.Print:
       j := 0
L17:   t0 := j < size
       iftrue t0 goto L18
       goto L19
L18:   t1 := number[j]
       param t1
       call System.out.println, 1
       t2 := j + 1
       j := t2
       goto L17
L19:   return 0

QS.Init:
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