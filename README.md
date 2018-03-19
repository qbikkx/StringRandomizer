# StringRandomizer

MVI Chain: View -> (Intent) -> 
            ViewModel -> (Action) -> 
             Proccessor -- Repository -- Proccessor -> (Result) -> 
              Reducer -> (ViewState) -> View
              
Три модуля: app - приложение. 
            base - утилиты / екстеншны / базовые классы. 
            data - репозиторий, в котором locale - Room, remote - пуст
