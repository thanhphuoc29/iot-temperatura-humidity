import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Layout from './layout';
import Home from './components/home/home';
import Profile from './components/profile/Profile';
import DataHistory from './components/DataHistory/DataHistory';
import ConfigHistory from './components/configHistory/ConfigHistory';
function App() {

  return (

    <div>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Layout></Layout>}>
            <Route path='' element={<Home></Home>}></Route>
            <Route path='/profile' element={<Profile></Profile>}></Route>
            <Route path='/data-history' element={<DataHistory></DataHistory>}></Route>
            <Route path='/config-history' element={<ConfigHistory></ConfigHistory>}></Route>
          </Route>
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
