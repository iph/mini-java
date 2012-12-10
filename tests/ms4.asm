
Test.main:
        li $t0, 0
        move $a0, $t0
        jal _new_object
        move $t0, $v0
        li $t1, 0
        move $a0, $t0
        move $a1, $t1
        jal Test2.Start
        move $t0, $v0
        move $a0, $t0
        jal _system_out_println
        jal _system_exit
Test2.Start:
        addi $sp, $sp, -8
        sw $ra, 0($sp)
        sw $fp, 4($sp)
        move $fp, $sp
        addi $sp, $sp, -8
        li $t0, 4
        li $t1, 5
        add $t0, $t0, $t1
        move $v0, $t0
Test2.Start_epilogue:
        move $sp, $fp
        lw $fp, -4($sp)
        lw $ra, 0($sp)
        addi $sp, $sp, 8
        jr $ra


_system_exit:
	li $v0, 10 #exit
	syscall
	
# Integer to print is in $a0. 
# Kills $v0 and $a0
_system_out_println:
	# print integer
	li  $v0, 1 
	syscall
	# print a newline
	li $a0, 10
	li $v0, 11
	syscall
	jr $ra
	
# $a0 = number of bytes to allocate
# $v0 contains address of allocated memory
_new_object:
	# sbrk
	li $v0, 9 
	syscall
	
	#initialize with zeros
	move $t0, $a0
	move $t1, $v0
_new_object_loop:
	beq $t0, $zero, _new_object_exit
	sb $zero, 0($t1)
	addi $t1, $t1, 1
	addi $t0, $t0, -1
	j _new_object_loop
_new_object_exit:
	jr $ra
	
# $a0 = number of bytes to allocate 
# $v0 contains address of allocated memory (with offset 0 being the size)	
_new_array:
	# add space for the size (1 integer)
	addi $a0, $a0, 4
	# sbrk
	li $v0, 9
	syscall
#initialize to zeros
	move $t0, $a0
	move $t1, $v0
_new_array_loop:
	beq $t0, $zero, _new_array_exit
	sb $zero, 0($t1)
	addi $t1, $t1, 1
	addi $t0, $t0, -1
	j _new_array_loop
_new_array_exit:
	#store the size (number of ints) in offset 0
	addi $t0, $a0, -4
	sra $t0, $t0, 2
	sw $t0, 0($v0)
	jr $ra


