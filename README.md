## About
This app fetches live currency exchange rates from the Frankfurter API 
and displays them in a RecyclerView list. Users can convert between 
12 major world currencies in real time.

## How It Works
1. On app launch, OkHttp makes a GET request to Frankfurter API
2. The JSON response is parsed to extract exchange rates
3. Rates are displayed in a RecyclerView with flags and % change
4. User enters an amount, selects from/to currency and taps Convert
5. The conversion formula used is:
   result = amount × (toRate / fromRate)

## API Used
- **Frankfurter API** — https://api.frankfurter.app
- Free, no API key required
- Base currency: USD
- Updates daily

## Supported Currencies
USD, EUR, GBP, JPY, CAD, AUD, CHF, CNY, INR, MXN, SGD, HKD

## Tech Stack
- Java
- Android RecyclerView
- OkHttp networking library
- Frankfurter open-source API
- Material Components UI

## Screenshots
<img width="1080" height="2400" alt="Screenshot_20260526_145823" src="https://github.com/user-attachments/assets/2d4a4ce1-e52c-4bec-afb6-6f7f80907900" />
<img width="1080" height="2400" alt="Screenshot_20260526_145858" src="https://github.com/user-attachments/assets/708cb7e4-0045-4105-961f-2e1480bcccc9" />
<img width="1080" height="2400" alt="Screenshot_20260526_145913" src="https://github.com/user-attachments/assets/e77366b1-16ef-41f2-9218-edf57bc11a40" />
<img width="1080" height="2400" alt="Screenshot_20260526_165359" src="https://github.com/user-attachments/assets/79cb0be4-20d1-432f-a791-f76b76e4a9f2" />
<img width="1080" height="2400" alt="Screenshot_20260526_165307" src="https://github.com/user-attachments/assets/6cf9f04f-e366-40a8-bd2c-5543aa1fbc66" />


## How to Run
1. Clone the repository
2. Open in Android Studio
3. Let Gradle sync complete
4. Run on emulator or physical device (requires internet)
