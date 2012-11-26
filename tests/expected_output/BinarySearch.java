BinarySearch.main:
       t0 := new BS
       param t0
       param 20
       t1 := call BS.Start, 2
       param t1
       call System.out.println, 1

BS.Start:
       param this
       param sz
       t0 := call BS.Init, 2
       aux01 := t0
       param this
       t1 := call BS.Print, 1
       aux02 := t1
       param this
       param 8
       t2 := call BS.Search, 2
       iftrue t2 goto L0
       param 0
       call System.out.println, 1
       goto L1
L0:    param 1
       call System.out.println, 1
L1:    param this
       param 19
       t3 := call BS.Search, 2
       iftrue t3 goto L2
       param 0
       call System.out.println, 1
       goto L3
L2:    param 1
       call System.out.println, 1
L3:    param this
       param 20
       t4 := call BS.Search, 2
       iftrue t4 goto L4
       param 0
       call System.out.println, 1
       goto L5
L4:    param 1
       call System.out.println, 1
L5:    param this
       param 21
       t5 := call BS.Search, 2
       iftrue t5 goto L6
       param 0
       call System.out.println, 1
       goto L7
L6:    param 1
       call System.out.println, 1
L7:    param this
       param 37
       t6 := call BS.Search, 2
       iftrue t6 goto L8
       param 0
       call System.out.println, 1
       goto L9
L8:    param 1
       call System.out.println, 1
L9:    param this
       param 38
       t7 := call BS.Search, 2
       iftrue t7 goto L10
       param 0
       call System.out.println, 1
       goto L11
L10:   param 1
       call System.out.println, 1
L11:   param this
       param 39
       t8 := call BS.Search, 2
       iftrue t8 goto L12
       param 0
       call System.out.println, 1
       goto L13
L12:   param 1
       call System.out.println, 1
L13:   param this
       param 50
       t9 := call BS.Search, 2
       iftrue t9 goto L14
       param 0
       call System.out.println, 1
       goto L15
L14:   param 1
       call System.out.println, 1
L15:   return 999

BS.Search:
       aux01 := 0
       bs01 := false
       t0 := length number
       right := t0
       t1 := right - 1
       right := t1
       left := 0
       var_cont := true
L16:   iftrue var_cont goto L17
       goto L18
L17:   t2 := left + right
       medium := t2
       param this
       param medium
       t3 := call BS.Div, 2
       medium := t3
       t4 := number[medium]
       aux01 := t4
       t5 := num < aux01
       iftrue t5 goto L19
       t6 := medium + 1
       left := t6
       goto L20
L19:   t7 := medium - 1
       right := t7
L20:   param this
       param aux01
       param num
       t8 := call BS.Compare, 3
       iftrue t8 goto L21
       var_cont := true
       goto L22
L21:   var_cont := false
L22:   t9 := right < left
       iftrue t9 goto L23
       nt := 0
       goto L24
L23:   var_cont := false
L24:   goto L16
L18:   param this
       param aux01
       param num
       t10 := call BS.Compare, 3
       iftrue t10 goto L25
       bs01 := false
       goto L26
L25:   bs01 := true
L26:   return bs01

BS.Div:
       count01 := 0
       count02 := 0
       t0 := num - 1
       aux03 := t0
L27:   t1 := count02 < aux03
       iftrue t1 goto L28
       goto L29
L28:   t2 := count01 + 1
       count01 := t2
       t3 := count02 + 2
       count02 := t3
       goto L27
L29:   return count01

BS.Compare:
       retval := false
       t0 := num2 + 1
       aux02 := t0
       t1 := num1 < num2
       iftrue t1 goto L30
       t2 := num1 < aux02
       t3 := ! t2
       iftrue t3 goto L31
       retval := true
       goto L32
L31:   retval := false
L32:   goto L33
L30:   retval := false
L33:   return retval

BS.Print:
       j := 1
L34:   t0 := j < size
       iftrue t0 goto L35
       goto L36
L35:   t1 := number[j]
       param t1
       call System.out.println, 1
       t2 := j + 1
       j := t2
       goto L34
L36:   param 99999
       call System.out.println, 1
       return 0

BS.Init:
       size := sz
       t0 := new int[], sz
       number := t0
       j := 1
       t1 := size + 1
       k := t1
L37:   t2 := j < size
       iftrue t2 goto L38
       goto L39
L38:   t3 := 2 * j
       aux01 := t3
       t4 := k - 3
       aux02 := t4
       t5 := aux01 + aux02
       number[j] := t5
       t6 := j + 1
       j := t6
       t7 := k - 1
       k := t7
       goto L37
L39:   return 0
