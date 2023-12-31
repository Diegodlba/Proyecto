        package com.grupo3.digitalbook.demo.service.impl;

        import com.grupo3.digitalbook.demo.entity.MarcaProducto;
        import com.grupo3.digitalbook.demo.entity.Producto;
        import com.grupo3.digitalbook.demo.entity.TipoProducto;
        import com.grupo3.digitalbook.demo.exception.BadRequestException;
        import com.grupo3.digitalbook.demo.exception.ResourceNotFoundException;
        import com.grupo3.digitalbook.demo.repository.IMarcaProductoRepository;
        import com.grupo3.digitalbook.demo.repository.IProductoRepository;
        import com.grupo3.digitalbook.demo.repository.ITipoProductoRepository;
        import com.grupo3.digitalbook.demo.service.IProductoService;
        import com.grupo3.digitalbook.demo.service.ITipoProductoService;
        import org.apache.log4j.Logger;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

        @Service

        public class ProductoService implements IProductoService {
            private final static Logger LOGGER = Logger.getLogger(ProductoService.class);

            @Autowired
            IProductoRepository iProductoRepository;

            @Autowired
            IMarcaProductoRepository iMarcaProductoRepository;

            @Autowired
            ITipoProductoRepository iTipoProductoRepository;


                public Producto guardarProducto(Producto productoNew) throws BadRequestException {
                    // Verifica si la marca existe en la base de datos
                    MarcaProducto marcaProducto = iMarcaProductoRepository.findByDescripcion(productoNew.getMarcaProducto().getDescripcion());
                    if (marcaProducto == null) {
                        // Si no existe, crea una nueva marca
                        marcaProducto = iMarcaProductoRepository.save(productoNew.getMarcaProducto());
                    }

                    // Verifica si el tipo existe en la base de datos
                    TipoProducto tipoProducto = iTipoProductoRepository.findByDescripcion(productoNew.getTipoProducto().getDescripcion());
                    if (tipoProducto == null) {
                        // Si no existe, crea un nuevo tipo
                        tipoProducto = iTipoProductoRepository.save(productoNew.getTipoProducto());
                    }

                // Asocia el producto a las marcas y tipos existentes
                productoNew.setMarcaProducto(marcaProducto);
                productoNew.setTipoProducto(tipoProducto);

                return iProductoRepository.save(productoNew);
            }




        public Optional<Producto> findProductoById(Long id) throws ResourceNotFoundException{

            Optional<Producto> producto = iProductoRepository.findById(id);
            if (producto.isPresent()){
                LOGGER.info("se encontro el producto");
                return producto;
            }else {
                LOGGER.error("no existe este producto");
                throw new ResourceNotFoundException("no existe este producto");
            }
        }

        public List<Producto> traertodos(){
            return iProductoRepository.findAll();
        }

        public Producto updateProducto(Producto productoNew,Long id)throws BadRequestException, ResourceNotFoundException{
            return iProductoRepository.findById(id).map(
                    producto -> {
                        producto.setId(productoNew.getId());
                        producto.setNombre(productoNew.getNombre());
                        producto.setDescripcion(productoNew.getDescripcion());
                        producto.setPrecio(productoNew.getPrecio());
                        producto.setImagenProductos(productoNew.getImagenProductos());
                        // producto.setImagen(productoNew.getImagen());
                        producto.setMarcaProducto(productoNew.getMarcaProducto());
                        producto.setTipoProducto(productoNew.getTipoProducto());
                        LOGGER.info("se actualizo el producto");
                        return iProductoRepository.save(producto);
                    }
            ).get();
        }

        public void eliminar(Long id) throws ResourceNotFoundException {
            if (iProductoRepository.existsById(id)) {
                iProductoRepository.deleteById(id);
                LOGGER.info("Producto eliminado correctamente");
            } else {
                LOGGER.error("No se pudo eliminar el producto");
                throw new ResourceNotFoundException("No se eliminó el producto");
            }
        }

    }
