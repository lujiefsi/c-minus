global F_28052
F_28052:
push	ebp
mov	ebp, esp
push	dword [ebp+8]
push	dword 0
pop	ebx
pop	eax
cmp	eax, ebx
sete	al
movzx	eax, al
push	dword eax
pop	eax
cmp	eax, 0
je	L1
push	dword [ebp+12]
pop	eax
leave
ret
jmp	L2
L1:
push	dword [ebp+8]
push	dword [ebp+12]
push	dword [ebp+12]
push	dword [ebp+8]
pop	ebx
pop	eax
mov	edx, eax
sar	edx, 31
idiv	ebx
push	dword eax
push	dword [ebp+8]
pop	ebx
pop	eax
imul	eax, ebx
push	dword eax
pop	ebx
pop	eax
sub	eax, ebx
push	dword eax
call	F_28052
add	esp, 8
push	dword eax
pop	eax
leave
ret
L2:
leave
ret
global _start
_start:
push	ebp
mov	ebp, esp
sub	esp, 4
sub	esp, 4
call	F_7362500
add	esp, 0
push	dword eax
lea	ebx, [ebp-4]
pop	eax
mov	dword [ebx], eax
call	F_7362500
add	esp, 0
push	dword eax
lea	ebx, [ebp-8]
pop	eax
mov	dword [ebx], eax
push	dword [ebp-4]
push	dword [ebp-8]
call	F_28052
add	esp, 8
push	dword eax
call	F_124565444
add	esp, 8
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
