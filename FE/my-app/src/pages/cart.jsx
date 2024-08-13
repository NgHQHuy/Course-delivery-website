import React, { useEffect, useState } from "react";
import "../styles/cart.css";
import { MdDelete } from "react-icons/md";

import axios from "axios";
import { useSelector } from "react-redux";
import { getBaseLoad } from "../redux/baseLoader.slice";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const Cart = () => {
  const navigate = useNavigate();
  const baseLoad = useSelector(getBaseLoad);

  const [cart, setCart] = useState([]);
  const fetchCart = async () => {
    let _cart = [];
    try {
      const res = await axios
        .get(`http://localhost:8083/api/cart/${baseLoad.user.userID}`)
        .then((res) => {
          if (res.status == 200 && res.data) {
            if (res.data.length > 0) {
              _cart = res.data;
              setCart(_cart);
            } else {
              navigate("/", toast.warn("Cart have nothing!"));
            }
          }
        });
    } catch (error) {
      toast.warn("Cart have nothing!");
      navigate("/");
    }
  };
  useEffect(() => {
    if (baseLoad.user.userID && baseLoad.user.role == "USER") {
      fetchCart();
    } else {
      navigate("/");
    }
  }, []);

  const removeCourse = async (id) => {
    try {
      const res = await axios.post("http://localhost:8083/api/cart/delete", {
        userId: baseLoad.user.userID,
        courseId: id,
      });
      res.data && res.data.message == "Success" && cart.length > 1
        ? setCart([...cart.filter((item) => item.courseId != id)])
        : navigate("/");
    } catch (error) {
      toast.error("Delete failed!");
    }
  };
  return (
    <div className="cart-container">
      <div className="cart-header">
        <span>Shopping Cart</span>
      </div>
      <div className="cart-body">
        <div className="item-list-container">
          <div className="num-of-item">
            {cart.length == 1
              ? cart.length + " course"
              : cart.length + " courses"}{" "}
            in Cart
          </div>
          <div className="item-list">
            {cart &&
              cart.length > 0 &&
              cart.map((item) => (
                <div className="item-container" key={item.id}>
                  <div className="item-thumbnail">
                    <div className="item-thumbnail-img"></div>
                  </div>
                  <div className="item-info">
                    <div className="item-overview">
                      <div className="item-title">{item.title}</div>
                      <div className="item-price">
                        {item.price.toLocaleString("it-IT", {
                          style: "currency",
                          currency: "VND",
                        })}
                      </div>
                    </div>
                    <div className="item-btn-remove">
                      <MdDelete
                        size={24}
                        style={{ color: "red", cursor: "pointer" }}
                        onClick={() => removeCourse(item.courseId)}
                      />
                    </div>
                  </div>
                </div>
              ))}
          </div>
        </div>
        <div className="sidebar-info"></div>
      </div>
    </div>
  );
};

export default Cart;
