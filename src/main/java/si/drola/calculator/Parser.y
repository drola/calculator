%language "Java"
%name-prefix "Calc"
%define parser_class_name "Parser"
%define package "si.drola.calculator"
%define public

%define throws Exception

%token <Double> REAL
%token <Value> UNIT
%token <Function> RFUNC
%token <Function> UFUNC
%token EXPONENT
%token MULTIPLY
%token MULTSTAR
%token DIVIDE
%token NUMDIV
%token SQRT
%token CUBEROOT
%token MULTMINUS
%token EOL
%token FUNCINV
%token SCANERROR
%token BADNUMBER


%type <Double> numexpr
%type <Value> expr
%type <Value> list
%type <Value> pexpr
%type <Value> unitexpr


%left ADD MINUS
%left UNARY
%left DIVIDE MULTSTAR
%left MULTIPLY MULTMINUS
%nonassoc '(' SQRT CUBEROOT RFUNC UNIT REAL UFUNC FUNCINV SCANERROR BADNUMBER
%right EXPONENT
%left NUMDIV

%code {

Value result = null;
public Value getResult() {
  return result;
}

}

%%
 input: EOL          { result = new Value(1); return YYACCEPT; }
      | unitexpr EOL { result = $1; return YYACCEPT; }
      | error        { return YYABORT; }
      ;

 unitexpr:  expr                    {$$ = $1;}
         |  DIVIDE list             {$$=$2.exp(-1);}
         ;

 expr: list                         { $$ = $1; }
     | MULTMINUS list %prec UNARY   { $$ = $2.multiply(new Value(-1)); }
     | MINUS list %prec UNARY       { $$ = $2.multiply(new Value(-1)); }
     | expr ADD expr                { $$ = $1.add($3);}
     | expr MINUS expr              { $$ = $1.substract($3);}
     | expr DIVIDE expr             { $$ = $1.divide($3);}
     | expr MULTIPLY expr           { $$ = $1.multiply($3); }
     | expr MULTSTAR expr           { $$ = $1.multiply($3); }
     ; 

 numexpr:  REAL                     { $$ = $1;         }
         | numexpr NUMDIV numexpr   { $$ = $1 / $3;    }
     ;

 pexpr: '(' expr ')'                { $$ = $2;  }
       ;

 /* list is a list of units, possibly raised to powers, to be multiplied
    together. */

 list:  numexpr                    { $$ = new Value($1);}
      | UNIT                       { $$ = $1; }
      | list EXPONENT list         { $$=$1.exp($3);}
      | list MULTMINUS list        { $$=$1.multiply($3);}
      | list list %prec MULTIPLY   { $$=$1.multiply($2);}
      | pexpr                      { $$=$1; }
      | SQRT pexpr                 { $$ = $2.exp(0.5);}
      | CUBEROOT pexpr             { $$ = $2.exp(1/3.0);}
      
/*      | RFUNC pexpr                { err = funcunit($2,$1); CHECK; $$=$2;}
      | UFUNC pexpr                { err = evalfunc($2,$1,0); CHECK; $$=$2;}
      | FUNCINV UFUNC pexpr        { err = evalfunc($3,$2,1); CHECK; $$=$3;} */
      
      | list EXPONENT MULTMINUS list %prec EXPONENT  
                                   { $$=$1.exp($4.multiply(new Value(-1)));}
      | list EXPONENT MINUS list %prec EXPONENT  
                                   { $$=$1.exp($4.multiply(new Value(-1))); }
      | BADNUMBER                  { throw new Exception("Bad number."); }
      | SCANERROR                  { throw new Exception("Scan error."); }        
   ;



