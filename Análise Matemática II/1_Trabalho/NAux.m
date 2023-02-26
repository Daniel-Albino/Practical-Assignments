function y =  NAux(f,a,b1,n1,y0)
%NRK4 Método de de Runge-Kutta de ordem 4 para resolução numérica de EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   y(i+1) = y(i)+1/6*(k1+(2*k2)+(2*k3)+k4, i=0,1,2,...,n
%   Onde: 
%       k1=h*f(t(i),y(i));
%       k2=h*f(t(i)+(h/2),y(i)+(k1/2));
%       k3=h*f(t(i)+(h/2),y(i)+(k2/2));
%       k4=h*f(t(i+1),y(i)+k3);

%INPUT:
%   f - função da EDO y'=f(t,y)
%   [a,b] - intervalo de valores da variável independente t
%   n - núnmero de subintervalos ou iterações do método
%   y0 - aproximação inicial y(a)=y0

%OUTPUT:
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i)
%

%Nuno Domingues  a2020109910@isec.pt
%Miguel Neves    a2020146521@isec.pt
%Daniel Albino   a2020134077@isec.pt
%
%Data: 15/04/2021

h = (b1-a)/n1;
t(1) = a;
y = zeros(1,n1+1);%pré-alocação de memória
y(1) = y0;

for i =1:n1
    t(i+1) = t(i)+h;
    k1=h*f(t(i),y(i));
    k2=h*f(t(i)+(h/2),y(i)+(k1/2));
    k3=h*f(t(i)+(h/2),y(i)+(k2/2));
    k4=h*f(t(i+1),y(i)+k3);
    
    y(i+1) = y(i)+1/6*(k1+(2*k2)+(2*k3)+k4);
end
end