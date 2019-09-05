import java.text.DecimalFormat

/** Задание 24: Итоговое задание
 *
 * 1. Создайте в программе для авиакомпании еще пару классов, описывающих различные модели самолетов.
 * Сделайте один из них грузовым, для этого создайте соответствующий интерфейс со свойством грузоподъёмность.
 *
 * 2. Создайте интерфейс в консоли для пользователя, где пользователь может запрашивать информацию по моделям самолетов.
 * Интерфейс должен отображать список команд, а пользователь в свою очередь выбирает номер команды.
 */
fun main(args: Array<String>) {
    println("Добро пожаловать в наш ангар!")

    // генерируем тестовые данные по каждому типу самолета
    val boeing747 = Boeing747(350)
    val superJet = SuperJet()
    val jumboJet3k = JumboJet()

    var isAgain = false // флаг для возможности сделать выбора пункта меню повторно
    while (!isAgain) {
        printMainMenu()

        var isValidChoice = false // флаг корректного выбора пункта главного меню
        while (!isValidChoice) {

            print("Ваш выбор:")
            when (readLine()!!.trim()) {
                "1" -> {
                    print("\n" + boeing747.info)
                    isValidChoice = true
                    isAgain = startAgainLoop()
                }
                "2" -> {
                    print("\n" + superJet.info)
                    isValidChoice = true
                    isAgain = startAgainLoop()
                }
                "3" -> {
                    print("\n" + jumboJet3k.info)
                    isValidChoice = true
                    isAgain = startAgainLoop()
                }
                "4" -> {
                    isValidChoice = true
                    isAgain = true
                }
                else -> println("Некорректный ввод! Попробуйте еще раз!")
            }
        }
    }
    println("\n" + "Спасибо за пользование нашим сервисом!")
}

/**
 * Вспомогательная функция, для печати главного меню. Немного облегчает читаемость кода в main-функции.
 */
fun printMainMenu() {
    println("Что желаете посмотреть? (введите цифру, в соответствии с пунктом меню)")
    println("1. Информацию по самолетам Boeing 747")
    println("2. Информацию по самолетам Sukhoi Super Jet")
    println("3. Информацию по самолетам Jumbo Jet 3000")
    println("4. Выход из приложения")
}

/**
 * Вспомогательная функция для главного меню, для возможности сделать несколько запросов в одной сессии.
 * Возвращает false если пользователь ввёл "1", true - в остальных случаях.
 */
fun startAgainLoop(): Boolean {
    print("\n" + "Хотите посмотреть еще? [1-да, (другой символ)-нет]: ")
    return readLine()!!.trim() != "1"
}

/**
 * Абстрактный класс-родитель для самолётов различных моделей.
 * Имеет свойства:
 * - maxFlightLength (максимальная дальность полета)
 * - fuelTankCapacity (предельный обьем топлива)
 * - fuelConsumption (расход топлива)
 */
abstract class Aircraft(protected val maxFlightLength: Int, protected val fuelTankCapacity: Int) {
    // вторичный конструктор для создания объектов класса с параметрами по умолчанию
    constructor() : this(9245, 280976)

    protected val fuelConsumption: Double
        get() = fuelTankCapacity.toDouble() / maxFlightLength
    open val info: String = "Aircraft info:\n" +
            "Maximum Flight Length = $maxFlightLength km\n" +
            "Fuel Tank Capacity = $fuelTankCapacity liters\n" +
            "Fuel Consumption = ${DecimalFormat("#.###").format(fuelConsumption)} L/km\n"

    // абстрактная реализация для названия самолета
    protected abstract val name: String
}

/**
 * Интерфейс для классов, описывающих самолеты со свойствами:
 * - passengerSeatsNumber: количество мест в салоне самолета (имеет дефолтный геттер)
 * - businessClassSeatsNumber: количество мест в салоне для пассажиров бизнес-класса (абстрактное)
 *
 * Также сюда перенесена абстрактная функция getAverageTicketPrice(), для того чтобы избавить
 * не-пассажирские самолеты, наследующиеся от класса Aircraft, реализовывать эту функцию.
 */
interface Passenger {
    val passengerSeatsNumber: Int
        get() = 250
    val businessClassSeatsNumber: Int
    fun getAverageTicketPrice(): Double
}

/**
 * Интерфейс для грузовых самолетов.
 * Имеет свойство maxPayload, описывающее максимальную грузоподъёмность самолета.
 */
interface Freight {
    val maxPayload: Double
}

/**
 * Класс-наследник от Aircraft для самолетов Boeing 747.
 * Первичный конструктор Boeing747 определен через вызов первичного конструктора Aircraft
 * с фиксированными значениями параметров maxFlightLength и fuelTankCapacity по умолчанию.
 * Реализует интерфейс Passenger, при этом переопределяет свойство passengerSeatsNumber, предоставляя
 * возможность вручную задать параметр при инициализации класса (игнорируя реализацию по умолчанию).
 * Также класс вынужденно переопределяет свойство businessClassSeatsNumber (т.к. оно абстрактное в интерфейсе Passenger)
 *
 * Ещё класс переопределяет свойство info, фактически дополняя свойство родителя своей дополнительной инфой.
 */
class Boeing747(seats: Int) : Aircraft(10299, 300499), Passenger {
    override val name = "Boeing 747"
    override val passengerSeatsNumber: Int = seats
    override val businessClassSeatsNumber: Int = seats / 10
    override val info: String = "$name " + super.info +
            "Number of passengers seats = $passengerSeatsNumber\n" +
            "Number of business-class seats = $businessClassSeatsNumber\n" +
            "Average ticket price = ${DecimalFormat("#.##").format(getAverageTicketPrice())} $\n"

    /**
     * Функция получения средней стоимости билета на самолет, исходя из внутренней бизнес-логики менеджмента Boeing.
     */
    override fun getAverageTicketPrice(): Double {
        return fuelTankCapacity * 2.34 / ((passengerSeatsNumber - businessClassSeatsNumber) * 5.6)
    }
}

/**
 * Еще один наследник Aircraft, также реализует интерфейс Passenger.
 * Но при этом не переопределяет его свойство passengerSeatsNumber.
 * При инициализации объекта данного класса значение свойства будет взято из геттера интерфейса Passenger.
 * Вынужденно переопределяет свойство businessClassSeatsNumber путем присваивания константного значения по умолчанию.
 *
 * Ещё класс переопределяет свойство info, фактически дополняя свойство родителя своей дополнительной инфой.
 */
class SuperJet : Aircraft(), Passenger {
    override val name = "Super Jet"
    override val businessClassSeatsNumber: Int = 10
    override val info: String = "$name " + super.info +
            "Number of passengers seats = $passengerSeatsNumber\n" +
            "Number of business-class seats = $businessClassSeatsNumber\n" +
            "Average ticket price = ${DecimalFormat("#.##").format(getAverageTicketPrice())} $\n"

    /**
     * Функция получения средней стоимости билета на самолет, исходя из внутренней бизнес-логики менеджмента Sukhoi.
     */
    override fun getAverageTicketPrice(): Double {
        return fuelTankCapacity * 1.55 / (passengerSeatsNumber * 11.45)
    }
}

/**
 * Класс грузового самолета.
 * Реализует интерфейс Freight, а значит описывает его свойство maxPayload.
 */
class JumboJet : Aircraft(13000, 495966), Freight {
    override val name = "Jumbo Jet 3000"
    override val maxPayload = 944.51
    override val info: String = "$name " + super.info +
            "Max weight capacity = $maxPayload t.\n"
}