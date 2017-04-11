section .bss
D_120:	resw 20
section .data
numbers db "0123456789", 0xA
inputchar db 0
section .text
global F_124565444
F_124565444:
push	ebp
mov	ebp, esp
sub esp, 4
mov dword [ebp-4], 0
jmp G2
G1:
add dword [ebp-4], 1
push edx
G2:
mov edx, 0
mov eax, [ebp+8]
mov ebx, 10
div ebx
mov [ebp+8], eax
cmp eax, 0
jnz G1
push edx
add dword [ebp-4], 1
jmp G3
G4:
sub dword [ebp-4], 1
pop edx
mov eax, 4
mov ebx, 1
mov ecx, numbers
add ecx, edx
mov edx, 1
int 80h
G3:
cmp dword [ebp-4], 0
jnz G4
mov eax, 4
mov ebx, 1
lea ecx, [numbers+10]
mov edx, 1
int 80h
leave
ret
global F_7362500
F_7362500:
push	ebp
mov	ebp, esp
sub esp, 4
mov dword [ebp-4], 0
mov byte [inputchar], 0
jmp G6
G5:
mov dword eax, [ebp-4]
mov ebx, 10
mul ebx
xor ecx, ecx
mov byte cl, [inputchar]
sub ecx, 48
add eax, ecx
mov dword [ebp-4], eax
G6:
mov eax, 03h
mov ebx, 00h
mov ecx, inputchar
mov edx, 01h
int 80h
cmp byte [inputchar], 0ah
jne G5
mov dword eax, [ebp-4]
leave
ret
global F_121656147
F_121656147:
push	ebp
mov	ebp, esp
sub	esp, 4
sub	esp, 4
sub	esp, 4
push	dword [ebp+12]
lea	ebx, [ebp-12]
pop	eax
mov	dword [ebx], eax
push	dword [ebp+12]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
push	dword [ebx]
lea	ebx, [ebp-8]
pop	eax
mov	dword [ebx], eax
push	dword [ebp+12]
push	dword 1
pop	ebx
pop	eax
lea	eax, [eax+ebx]
push	dword eax
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
jmp	L1
L2:
push	dword [ebp-4]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
push	dword [ebx]
push	dword [ebp-8]
pop	ebx
pop	eax
cmp	eax, ebx
setl	al
movzx	eax, al
push	dword eax
pop	eax
cmp	eax, 0
je	L3
push	dword [ebp-4]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
push	dword [ebx]
lea	ebx, [ebp-8]
pop	eax
mov	dword [ebx], eax
push	dword [ebp-4]
lea	ebx, [ebp-12]
pop	eax
mov	dword [ebx], eax
L3:
push	dword [ebp-4]
push	dword 1
pop	ebx
pop	eax
lea	eax, [eax+ebx]
push	dword eax
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
L1:
push	dword [ebp-4]
push	dword [ebp+8]
pop	ebx
pop	eax
cmp	eax, ebx
setl	al
movzx	eax, al
push	dword eax
pop	eax
cmp	eax, 1
je	L2
push	dword [ebp-12]
pop	eax
leave
ret
leave
ret
global F_501396
F_501396:
push	ebp
mov	ebp, esp
sub	esp, 4
sub	esp, 4
sub	esp, 4
push	dword [ebp+12]
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
jmp	L4
L5:
push	dword [ebp+16]
push	dword [ebp-4]
push	dword [ebp+8]
call	F_121656147
add	esp, 12
push	dword eax
lea	ebx, [ebp-8]
pop	eax
mov	dword [ebx], eax
push	dword [ebp-8]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
push	dword [ebx]
lea	ebx, [ebp-12]
pop	eax
mov	dword [ebx], eax
push	dword [ebp-4]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
push	dword [ebx]
push	dword [ebp-8]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
pop	eax
mov	dword [ebx], eax
push	dword [ebp-12]
push	dword [ebp-4]
pop	eax
mov	ebx, [ebp+16]
imul	eax, 4
add	ebx, eax
pop	eax
mov	dword [ebx], eax
push	dword [ebp-4]
push	dword 1
pop	ebx
pop	eax
lea	eax, [eax+ebx]
push	dword eax
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
L4:
push	dword [ebp-4]
push	dword [ebp+8]
push	dword 1
pop	ebx
pop	eax
sub	eax, ebx
push	dword eax
pop	ebx
pop	eax
cmp	eax, ebx
setl	al
movzx	eax, al
push	dword eax
pop	eax
cmp	eax, 1
je	L5
leave
ret
global _start
_start:
push	ebp
mov	ebp, esp
sub	esp, 4
push	dword 0
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
jmp	L6
L7:
call	F_7362500
add	esp, 0
push	dword eax
push	dword [ebp-4]
pop	eax
mov	ebx, 0
imul	eax, 4
add	ebx, eax
lea	ebx, [D_120+ebx]
pop	eax
mov	dword [ebx], eax
push	dword [ebp-4]
push	dword 1
pop	ebx
pop	eax
lea	eax, [eax+ebx]
push	dword eax
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
L6:
push	dword [ebp-4]
push	dword 10
pop	ebx
pop	eax
cmp	eax, ebx
setl	al
movzx	eax, al
push	dword eax
pop	eax
cmp	eax, 1
je	L7
push	dword D_120
push	dword 0
push	dword 10
call	F_501396
add	esp, 12
push	dword 0
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
jmp	L8
L9:
push	dword [ebp-4]
pop	eax
mov	ebx, 0
imul	eax, 4
add	ebx, eax
push	dword [D_120+ebx]
call	F_124565444
add	esp, 4
push	dword [ebp-4]
push	dword 1
pop	ebx
pop	eax
lea	eax, [eax+ebx]
push	dword eax
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
L8:
push	dword [ebp-4]
push	dword 10
pop	ebx
pop	eax
cmp	eax, ebx
setl	al
movzx	eax, al
push	dword eax
pop	eax
cmp	eax, 1
je	L9
push	dword 0
pop	eax
leave
mov ebx, eax
mov eax,1
int 80h
leave
mov ebx, eax
mov eax,1
int 80h
